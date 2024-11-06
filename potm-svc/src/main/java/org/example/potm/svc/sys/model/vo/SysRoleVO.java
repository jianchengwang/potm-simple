package org.example.potm.svc.sys.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.example.potm.framework.pojo.VO;

/**
 * @author jianchengwang
 * @date 2023/11/8
 */
@Schema(description = "管理端-系统角色-VO")
@Data
public class SysRoleVO implements VO {
    @Schema(description = "编号")
    private Long id;
    @Schema(description = "角色标识")
    private String roleCode;
    @Schema(description = "角色名称")
    private String roleName;
    @Schema(description = "备注")
    private String remark;
}
