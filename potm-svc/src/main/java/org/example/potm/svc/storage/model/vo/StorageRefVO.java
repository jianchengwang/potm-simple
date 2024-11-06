package org.example.potm.svc.storage.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;
import org.example.potm.framework.pojo.VO;
import org.example.potm.svc.storage.constant.FileModuleEnum;

@Data
@Accessors(chain = true)
@Schema(description = "对象存储关联表-VO")
public class StorageRefVO implements VO {

    /**
     * 关联编号
     */
    @Schema(description = "关联编号")
    private Long refId;

    /**
     * 文件编号
     */
    @Schema(description = "文件编号")
    private Long fileId;

    /**
     * 文件库类型
     */
    @Schema(description = "文件库类型")
    private FileModuleEnum refModule;


    /**
     * 附加条件
     */
    @Schema(description = "附加条件")
    private Integer aq;
}
