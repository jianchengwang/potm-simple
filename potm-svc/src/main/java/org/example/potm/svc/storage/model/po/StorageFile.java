package org.example.potm.svc.storage.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;
import org.example.potm.framework.pojo.PO;
import org.example.potm.plugins.storage.autoconfigure.properties.StoreType;

import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
@TableName("storage_file")
public class StorageFile implements PO {

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 原始文件名
     */
    private String originalFileName;

    /**
     * 存储文件名
     */
    private String storageFileName;

    /**
     * mime类型
     */
    private String mimeType;

    /**
     * 文件后缀名
     */
    private String suffix;

    /**
     * hash
     */
    private String hash;

    /**
     * 对象大小
     */
    private Long size;

    /**
     * 存储对象key或者路径
     */
    private String path;

    /**
     * 文件库标识
     */
    private String module;

    /**
     * 存储平台
     */
    private StoreType storeType;

    /**
     * 创建者
     */
    private Long createUser;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}
