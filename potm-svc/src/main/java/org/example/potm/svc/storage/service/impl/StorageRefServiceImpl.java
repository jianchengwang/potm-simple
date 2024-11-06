package org.example.potm.svc.storage.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.potm.svc.storage.constant.FileModuleEnum;
import org.example.potm.svc.storage.dao.StorageRefDao;
import org.example.potm.svc.storage.model.po.StorageRef;
import org.example.potm.svc.storage.model.vo.StorageFileVO;
import org.example.potm.svc.storage.service.StorageFileService;
import org.example.potm.svc.storage.service.StorageRefService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class StorageRefServiceImpl extends ServiceImpl<StorageRefDao, StorageRef> implements StorageRefService {
    private final StorageFileService storageFileService;

    @Override
    public List<StorageFileVO> getRefFileList(String refId, FileModuleEnum fileModule, Integer aq) {
        return this.getRefFileList(Collections.singletonList(refId), fileModule, aq);
    }

    @Override
    public List<StorageFileVO> getRefFileList(List<String> refIdList, FileModuleEnum fileModule, Integer aq) {
        if(CollectionUtils.isEmpty(refIdList) || fileModule == null) {
            return Collections.emptyList();
        }
        return baseMapper.selectFileList(refIdList, fileModule.getValue(), aq);
    }

    @Transactional(rollbackFor = Throwable.class)
    @Override
    public void updateRefFileList(String refId, FileModuleEnum fileModule, Integer aq, List<Long> fileIdList, boolean emptyExisted) {
        Map<String, List<Long>> refMap = new HashMap<>();
        refMap.put(refId, fileIdList);
        updateRefFileMap(refMap, fileModule, aq, emptyExisted);
    }

    @Transactional(rollbackFor = Throwable.class)
    @Override
    public void updateRefFileMap(Map<String, List<Long>> refMap, FileModuleEnum fileModule, Integer aq, boolean emptyExisted) {
        if(refMap == null || refMap.isEmpty()) {
            return;
        }
        List<Long> repeatRefFileIdList = new ArrayList<>();
        List<StorageRef> refFileList = new ArrayList<>();
        refMap.forEach((k, v) -> {
            if (emptyExisted) {
                removeByRefIds(fileModule, aq, Collections.singletonList(k), v);
            }
            if (CollectionUtils.isNotEmpty(v)) {
                repeatRefFileIdList.addAll(baseMapper.repeatRefFileIdList(k, v));
                refFileList.addAll(v.stream().map(fileId -> {
                    StorageRef refFile = new StorageRef();
                    refFile.setRefId(k);
                    refFile.setRefModule(fileModule);
                    refFile.setFileId(fileId);
                    if (aq != null) {
                        refFile.setAq(aq);
                    }
                    return refFile;
                }).toList());
            }

        });
        if (CollectionUtils.isNotEmpty(repeatRefFileIdList)) {
            baseMapper.deleteBatchIds(repeatRefFileIdList);
        }
        if (CollectionUtils.isNotEmpty(refFileList)) {
            this.saveBatch(refFileList);
        }
    }

    @Transactional(rollbackFor = Throwable.class)
    @Override
    public void removeByRefIds(FileModuleEnum fileModule, Integer aq, List<String> refIds, List<Long> excludeFileIdList) {
        if(CollectionUtils.isEmpty(refIds) || fileModule == null) {
            return;
        }
        LambdaQueryWrapper<StorageRef> query = Wrappers.lambdaQuery();
        query.eq(StorageRef::getRefModule, fileModule).in(StorageRef::getRefId, refIds);
        if (aq != null) {
            query.eq(StorageRef::getAq, aq);
        }
        List<Long> fileIdList = baseMapper.selectList(query).stream().map(StorageRef::getFileId).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(excludeFileIdList)) {
            fileIdList = fileIdList.stream().filter(fid -> !excludeFileIdList.contains(fid)).collect(Collectors.toList());
        }
        if (CollectionUtils.isNotEmpty(fileIdList)) {
            storageFileService.removeByIds(fileIdList);
        }
        LambdaUpdateWrapper<StorageRef> wrapper = Wrappers.lambdaUpdate();
        if (aq != null) {
            wrapper.eq(StorageRef::getAq, aq);
        }
        wrapper.eq(StorageRef::getRefModule, fileModule).in(StorageRef::getRefId, refIds);
        baseMapper.delete(wrapper);
    }
}
