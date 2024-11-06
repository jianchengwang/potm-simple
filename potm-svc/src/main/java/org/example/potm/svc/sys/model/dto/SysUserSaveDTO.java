package org.example.potm.svc.sys.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.example.potm.framework.config.permission.user.UserScopeEnum;
import org.example.potm.framework.config.permission.user.UserStatusEnum;
import org.example.potm.framework.pojo.DTO;

import java.util.List;

/**
 * @author jianchengwang
 * @date 2023/4/10
 */
@Schema(description = "管理端-系统用户保存-DTO")
@Data
public class SysUserSaveDTO implements DTO {
    @Schema(description = "用户编号")
    private Long id;

    @NotEmpty(message = "用户名不能为空")
    @Schema(description = "用户名")
    private String username;

    @Schema(description = "原始密码")
    private String password;

    @NotEmpty(message = "昵称不能为空")
    @Schema(description = "昵称")
    private String nickname;

    @NotEmpty(message = "手机号不能为空")
    @Schema(description = "手机号")
    private String mobile;

    @NotNull
    @Schema(description = "用户归属")
    private UserScopeEnum userScope;

    @Schema(description = "用户状态")
    private UserStatusEnum userStatus;

    @Schema(description = "部门编号")
    private String deptId;

    @Schema(description = "角色编号集合")
    private List<Long> roleIds;

    private String avatar;

    private String workUnit;

    private Integer userType;

    private String registerWay;

    private String projectId;
}
