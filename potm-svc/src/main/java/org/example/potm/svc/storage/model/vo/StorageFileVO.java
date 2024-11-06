package org.example.potm.svc.storage.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.example.potm.framework.pojo.VO;
import org.example.potm.plugins.storage.autoconfigure.properties.StoreType;
import org.example.potm.plugins.storage.pojo.SObject;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Accessors(chain = true)
@Schema(description = "存储对象-VO")
public class StorageFileVO implements VO {

    @Schema(description = "ID")
    private Long id;

    @Schema(description = "原始文件名")
    private String originalFileName;

    @Schema(description = "存储文件名")
    private String storageFileName;

    @Schema(description = "mime类型")
    private String mimeType;

    @Schema(description = "文件后缀名")
    private String suffix;

    @Schema(description = "hash")
    private String hash;

    @Schema(description = "对象大小")
    private Long size;

    @Schema(description = "存储对象key")
    private String path;

    @Schema(description = "存储模型库")
    private String module;

    @Schema(description = "存储平台")
    private StoreType storeType;

    @Schema(description = "关联编号", hidden = true)
    private String refId;

    @Schema(description = "关联编号的附加条件", hidden = true)
    private Integer aq;

    @Schema(description = "创建者")
    private Long createUser;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    public StorageFileVO(SObject sObject, StoreType storeType, String module) {
        this.setOriginalFileName(sObject.getOriginalFileName());
        this.setStorageFileName(sObject.getName());
        this.setMimeType(sObject.getMimeType());
        this.setSuffix(sObject.getSuffix());
        this.setPath(sObject.getKey());
        this.setHash(sObject.getHash());
        this.setSize(sObject.getSize());
        this.setModule(module);
        this.setStoreType(storeType);
    }
}
