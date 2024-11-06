package org.example.potm.svc.storage.service.impl;

import cn.hutool.core.io.FileUtil;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import org.example.potm.svc.storage.constant.FileModuleEnum;
import org.example.potm.svc.storage.model.po.StorageFile;
import org.example.potm.svc.storage.model.vo.StorageFileVO;
import org.example.potm.svc.storage.service.IFileModuleProvider;
import org.example.potm.svc.storage.service.StorageFileService;
import org.example.potm.svc.storage.service.StorageRefService;
import org.example.potm.svc.storage.service.StorageService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.example.potm.framework.exception.ClientException;
import org.example.potm.framework.exception.FrameworkErrorCode;
import org.example.potm.framework.pojo.PojoConverter;
import org.example.potm.plugins.storage.adapter.StorageAdapter;
import org.example.potm.plugins.storage.autoconfigure.properties.StoreType;
import org.example.potm.plugins.storage.pojo.PutObject;
import org.example.potm.plugins.storage.pojo.SObject;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Slf4j
public class StorageServiceImpl implements StorageService {

    private final StorageAdapter storageAdapter;
    private final StorageFileService storageFileService;
    private final StorageRefService storageRefService;
    private final ObjectProvider<List<IFileModuleProvider>> fileModuleProvider;
    private Map<FileModuleEnum, IFileModuleProvider> fileModuleProviderMap;
    public StorageServiceImpl(StorageAdapter storageAdapter, StorageFileService storageFileService, StorageRefService storageRefService, ObjectProvider<List<IFileModuleProvider>> fileModuleProvider, Map<FileModuleEnum, IFileModuleProvider> fileModuleProviderMap) {
        this.storageAdapter = storageAdapter;
        this.storageFileService = storageFileService;
        this.storageRefService = storageRefService;
        this.fileModuleProvider = fileModuleProvider;
        this.fileModuleProviderMap = fileModuleProviderMap;
    }

    @Override
    public Set<FileModuleEnum> getModules() {
        return fileModuleProviderMap.keySet();
    }

    @Transactional(rollbackFor = Throwable.class)
    @Override
    public List<SObject> upload(FileModuleEnum fileModule, String prefix, boolean forcedPut, boolean insertDb,
                                MultipartFile... files) {
        if(files.length == 0) {
            throw new ClientException("上传文件不能为空", FrameworkErrorCode.PARAM_VALIDATE_ERROR);
        }

        List<PutObject> batchPutObjectList = Arrays.stream(files).map(file -> {
            try {
                String originFileName = file.getOriginalFilename();
                // 文件上传时，Chrome和IE/Edge对于originalFilename处理不同
                // Chrome 会获取到该文件的直接文件名称，IE/Edge会获取到文件上传时完整路径/文件名
                originFileName = FileUtil.getName(originFileName);
                return new PutObject(
                        originFileName,
                        fullPath(file, prefix, fileModule),
                        file.getInputStream()
                );
            } catch (IOException e) {
                throw new ClientException(null, e);
            }
        }).collect(Collectors.toList());
        List<SObject> sObjectList = storageAdapter.doBatchPut(batchPutObjectList, forcedPut);

        insertDb = fileModule.getInsertDb() == null ? insertDb : fileModule.getInsertDb();
        if(insertDb) {
            // 插入数据表
            StoreType storeType = storageAdapter.getStoreType();
            List<StorageFileVO> fileList = sObjectList.stream()
                    .map(sObject -> new StorageFileVO(sObject, storeType, fileModule.name()))
                    .collect(Collectors.toList());
            List<StorageFile> savedList = PojoConverter.conventList(fileList, StorageFile.class);
            storageFileService.saveBatch(savedList);

            // 得到dbId
            sObjectList.forEach(sObject -> {
                String dbId = savedList.stream().filter(file -> file.getPath().equals(sObject.getKey()))
                        .findFirst().get().getId().toString();
                sObject.setDbId(dbId);
            });
        }

        return sObjectList;
    }

    @Override
    public SObject download(String fullPath) {
        return storageAdapter.doGet(fullPath);
    }

    @Override
    public SObject downloadByDbId(String dbId) {
        if(StringUtils.isEmpty(dbId)) {
            throw new ClientException("dbId不能为空", null);
        }
        StorageFile file = storageFileService.getById(dbId);
        if(file == null) {
            throw new ClientException("存储对象不存在", null);
        }
        SObject sObject = download(file.getPath());
        sObject.setOriginalFileName(file.getOriginalFileName());
        return sObject;
    }

    @Override
    public List<SObject> downloadByRefId(String refId, FileModuleEnum fileModule) {
        List<SObject> sObjectList = new ArrayList<>();
        List<StorageFileVO> storageFileList = storageRefService.getRefFileList(refId, fileModule, null);
        if(CollectionUtils.isNotEmpty(storageFileList)) {
            for(StorageFileVO sf: storageFileList) {
                SObject sObject = this.download(sf.getPath());
                sObject.setOriginalFileName(sf.getOriginalFileName());
                sObjectList.add(sObject);
            }
        }
        return sObjectList;
    }

    private String fullPath(MultipartFile file, String prefix, FileModuleEnum fileModule) {
        if (fileModuleProviderMap == null) {
            fileModuleProviderMap = Objects.requireNonNull(fileModuleProvider
                            .getIfAvailable())
                    .stream()
                    .collect(Collectors.toMap(IFileModuleProvider::getModule, Function.identity()));
        }
        IFileModuleProvider fileModuleService = fileModuleProviderMap.get(fileModule);
        if(fileModuleService == null) {
            fileModuleService = () -> fileModule;
        }
        return fileModuleService.buildPath(FileUtil.getName(file.getOriginalFilename()), prefix);
    }

}
