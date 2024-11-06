package org.example.potm.svc.storage.model.po;

import com.baomidou.mybatisplus.annotation.*;
import org.example.potm.svc.storage.constant.FileModuleEnum;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
@TableName("storage_ref")
public class StorageRef {

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 关联编号
     */
    private String refId;

    /**
     * 文件编号
     */
    private Long fileId;

    /**
     * 文件库类型
     */
    private FileModuleEnum refModule;

    /**
     * 附加条件
     */
    private Integer aq;

    /**
     * 创建者
     */
    @TableField(fill = FieldFill.INSERT)
    private Long createUser;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
