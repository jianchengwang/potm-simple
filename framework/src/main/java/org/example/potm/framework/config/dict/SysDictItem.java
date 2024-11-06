package org.example.potm.framework.config.dict;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.example.potm.framework.pojo.IBaseEnum;
import org.example.potm.framework.pojo.PO;
import org.example.potm.framework.pojo.VO;

import java.util.List;

/**
 * @author jianchengwang
 * @date 2023/4/11
 */
@Data
@TableName("sys_dict_item")
@Schema(description = "系统字典项-VO")
public class SysDictItem implements PO, VO {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    @Schema(description = "服务名")
    private String svcName;
    @Schema(description = "字典key")
    private String dictKey;
    @Schema(description = "字典项值")
    private String value;
    @Schema(description = "字典项标签")
    private String label;
    @Schema(description = "字典项类型")
    private String type;
    @Schema(description = "字典项颜色")
    private String color;
    @Schema(description = "字典项扩展信息")
    private String ext;
    @Schema(description = "字典项排序")
    private Integer sortOrder;
    @Schema(description = "字典项备注")
    private String remark;
    @Schema(description = "系统内置")
    private Boolean systemFlag;
    @Schema(description = "上级字典项值")
    private String parentItemValue;
    @TableField(exist = false)
    private List<SysDictItem> children;

    public SysDictItem() {
    }

    public SysDictItem(String svcName, String dictKey, String value, String label, Integer sortOrder, Boolean systemFlag) {
        this.svcName = svcName;
        this.dictKey = dictKey;
        this.value = value;
        this.label = label;
        this.sortOrder = sortOrder;
        this.systemFlag = systemFlag;
    }

    public SysDictItem(String svcName, String dictKey, String value, String label, Integer sortOrder, Boolean systemFlag, String remark) {
        this(svcName, dictKey, value, label, sortOrder, systemFlag);
        this.remark = remark;
    }


    public SysDictItem(String svcName, String dictKey, String value, String label, Integer sortOrder, Boolean systemFlag, String remark, String color) {
        this(svcName, dictKey, value, label, sortOrder, systemFlag, remark);
        this.color = color;
    }

    public SysDictItem(String svcName, String dictKey, String value, String label, Integer sortOrder, Boolean systemFlag, String remark, String color, String parentItemValue) {
        this(svcName, dictKey, value, label, sortOrder, systemFlag, remark, color);
        this.parentItemValue = parentItemValue;
    }

    public SysDictItem(String svcName, String dictKey, IBaseEnum itemEnum, Integer sortOrder) {
        this.svcName = svcName;
        this.dictKey = dictKey;
        this.value = itemEnum.getValue().toString();
        this.label = itemEnum.getDescription().toString();
        this.remark = itemEnum.getDescription().toString();
        this.type = itemEnum.getName();
        this.sortOrder = sortOrder;
        this.systemFlag = true;
    }
}
