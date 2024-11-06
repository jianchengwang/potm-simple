package org.example.potm.framework.config.permission.user;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import org.example.potm.framework.pojo.PO;
import lombok.Data;

/**
 * @author jianchengwang
 * @date 2023/3/30
 */
@Data
@TableName("sys_user")
public class SysUser implements PO {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    private String username;
    private String nickname;
    private String mobile;
    private String password;
    private String passwordSalt;
    private UserScopeEnum userScope;
    private UserStatusEnum userStatus;
    private String deptId;

    private String avatar;

    private String workUnit;

    private Integer userType;
    // getters and setters

    private String registerWay;
    private String openid;
    private String unionid;
    private String mpOpenid;
}
