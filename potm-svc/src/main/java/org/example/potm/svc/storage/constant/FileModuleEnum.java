package org.example.potm.svc.storage.constant;

import lombok.Getter;
import org.example.potm.framework.config.dict.DictEnum;
import org.example.potm.framework.config.dict.DictKeyEnum;
import org.example.potm.framework.pojo.IBaseEnum;

import static org.example.potm.plugins.storage.KeyGenerator.Predefined.BY_DATETIME;
import static org.example.potm.plugins.storage.KeyGenerator.Predefined.Predefined;

@DictEnum(dictKey = DictKeyEnum.file_module)
@Getter
public enum FileModuleEnum implements IBaseEnum<String> {
    PUBLIC("1", "公开库", "public", BY_DATETIME, false, null),
    ;

    private final String value;
    private final String description;
    private final String dir; // 存放目录
    private final Predefined keyGeneratorPredefined; // 生成规则
    private final boolean fileNameAsKey; // 文件名作为存储对象key
    private final Boolean insertDb; // 是否插入数据库

    FileModuleEnum(String value, String description, String dir, Predefined keyGeneratorPredefined,
                   boolean fileNameAsKey, Boolean insertDb) {
        this.value = value;
        this.description = description;
        this.dir = dir;
        this.keyGeneratorPredefined = keyGeneratorPredefined;
        this.fileNameAsKey = fileNameAsKey;
        this.insertDb = insertDb;
    }
}
