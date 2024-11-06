package org.example.potm.svc.sys.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.example.potm.framework.config.permission.user.UserScopeEnum;
import org.example.potm.framework.config.permission.user.UserStatusEnum;
import org.example.potm.framework.pojo.VO;

import java.util.List;

/**
 * @author jianchengwang
 * @date 2023/4/8
 */
@Schema(description = "管理端-系统用户-VO")
@Data
public class SysUserVO implements VO {
    @Schema(description = "编号")
    private Long id;

    @Schema(description = "账户名")
    private String username;

    @Schema(description = "昵称")
    private String nickname;

    @Schema(description = "手机号")
    private String mobile;

    @Schema(description = "用户归属")
    private UserScopeEnum userScope;

    @Schema(description = "用户状态")
    private UserStatusEnum userStatus;

    @Schema(description = "部门编号")
    private String deptId;

    @Schema(description = "角色集合")
    private List<String> roles;

    @Schema(description = "菜单集合")
    private List<String> menus;

    @Schema(description = "权限集合")
    private List<String> permissions;

    // 扩展信息
    @Schema(description = "所属部门")
    private String deptName;


    private String avatar;

    private String workUnit;

    private Integer userType;

    private String registerWay;
}
