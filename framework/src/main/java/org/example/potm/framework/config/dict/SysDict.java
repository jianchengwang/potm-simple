package org.example.potm.framework.config.dict;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.example.potm.framework.pojo.PO;
import org.example.potm.framework.pojo.VO;

import java.util.List;

/**
 * @author jianchengwang
 * @date 2023/4/11
 */
@Data
@TableName("sys_dict")
@Schema(description = "系统字典-VO")
public class SysDict implements PO, VO {
    @TableId(value = "id", type = IdType.AUTO)
    @Schema(description = "ID")
    private Long id;
    @Schema(description = "服务名")
    private String svcName;
    @Schema(description = "字典标识")
    private String dictKey;
    @Schema(description = "字典描述")
    private String description;
    @Schema(description = "字典备注")
    private String remark;
    @Schema(description = "是否是系统内置字典")
    private Boolean systemFlag;
    @Schema(description = "是否是枚举字典")
    private Boolean enumFlag;
    @TableField(exist = false)
    private Integer sortOrder;
    @TableField(exist = false)
    @Schema(description = "字典项集合")
    private List<SysDictItem> itemList;

    public SysDict() {
    }

    public SysDict(String svcName, String dictKey, String description, String remark, Boolean systemFlag, Boolean enumFlag, Integer sortOrder) {
        this.svcName = svcName;
        this.dictKey = dictKey;
        this.description = description;
        this.remark = remark;
        this.remark = description;
        this.systemFlag = systemFlag;
        this.enumFlag = enumFlag;
        this.sortOrder = sortOrder;
    }
}
