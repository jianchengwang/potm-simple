package org.example.potm.svc.sys.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "管理端-系统模块-字典项-DTO")
public class SysDictItemDTO {
    @Schema(description = "字典项值")
    private String value;
    @Schema(description = "字典项标签")
    private String label;
    @Schema(description = "字典项颜色")
    private String color;
    @Schema(description = "字典项扩展信息")
    private String ext;
    @Schema(description = "备注")
    private String remark;
    @Schema(description = "子字典项列表")
    private List<SysDictItemDTO> children;
}
