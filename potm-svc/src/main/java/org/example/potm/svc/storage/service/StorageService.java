package org.example.potm.svc.storage.service;

import org.example.potm.plugins.storage.pojo.SObject;
import org.example.potm.svc.storage.constant.FileModuleEnum;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;

public interface StorageService {
    /**
     * 获取文件模型集合
     *
     * @return 模型集合
     */
    Set<FileModuleEnum> getModules();

    List<SObject> upload(FileModuleEnum fileModule, String prefix, boolean forcedPut, boolean insertDb, MultipartFile... files);

    SObject download(String fullPath);

    SObject downloadByDbId(String dbId);

    List<SObject> downloadByRefId(String refId, FileModuleEnum fileModule);
}
