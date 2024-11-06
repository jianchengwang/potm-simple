package org.example.potm.framework.config.permission.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author jianchengwang
 * @date 2023/3/31
 */
@Data
public class TokenUser implements Serializable {
    @Schema(description = "主键")
    private Long id;

    @Schema(description = "用户名")
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

    @Schema(description = "访问token")
    private String accessToken;

    @Schema(description = "角色集合")
    private List<String> roles;

    @Schema(description = "菜单集合")
    private List<String> menus;

    @Schema(description = "权限集合")
    private List<String> permissions;

    @Schema(description = "小程序openid")
    private String openid;

    @Schema(description = "unionid")
    private String unionid;

    @Schema(description = "公众号openid")
    private String mpOpenid;

    @Schema(description = "头像")
    private String avatar;
}
