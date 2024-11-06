package org.example.potm.svc.sys.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import org.example.potm.framework.pojo.DTO;

import java.util.List;

@Schema(description = "管理端-系统角色保存-DTO")
@Data
public class SysRoleSaveDTO implements DTO {
    @Schema(description = "主键")
    private Long id;

    @Schema(description = "角色标识")
    @NotEmpty(message = "角色名标识")
    private String roleCode;

    @Schema(description = "角色名")
    @NotEmpty(message = "角色名不能为空")
    private String roleName;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "菜单集合")
    private List<String> menuIds;

    @Schema(description = "部门集合")
    private List<String> deptIds;

}
