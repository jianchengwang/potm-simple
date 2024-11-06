package org.example.potm.svc.sys.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import org.example.potm.framework.pojo.DTO;

/**
 * @author jianchengwang
 * @date 2023/4/10
 */
@Schema(description = "管理端-系统用户修改密码-DTO")
@Data
public class SysUserUpdatePasswordDTO implements DTO {

    @NotEmpty
    @Schema(description = "原始密码")
    private String oldPassword;

    @NotEmpty
    @Schema(description = "新密码")
    private String newPassword;
}
