package org.example.potm.svc.storage.service;

import org.example.potm.svc.storage.constant.FileModuleEnum;
import org.example.potm.plugins.storage.KeyGenerator;
import org.example.potm.plugins.storage.KeyProvider;

public interface IFileModuleProvider {
    /**
     * 获取文件模型
     *
     * @return 文件模型
     */
    FileModuleEnum getModule();

    /**
     * 构建路径
     *
     * @param originalFileName 文件名
     * @param refKey 关联对象标识
     * @return 路径
     */
    default String buildPath(String originalFileName, String refKey) {
        KeyGenerator.Predefined predefined = KeyGenerator.Predefined.PLAIN;
        FileModuleEnum module = getModule();
        if(module != null && module.getKeyGeneratorPredefined()!=null) {
            predefined = module.getKeyGeneratorPredefined();
        }
        assert module != null;
        return predefined.getKey(originalFileName, KeyProvider.DEF_PROVIDER, module.getDir(), refKey, module.isFileNameAsKey());
    }

}
