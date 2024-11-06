package org.example.potm.svc.sys.model.query;

import io.swagger.v3.oas.annotations.Parameter;
import lombok.Getter;
import lombok.Setter;
import org.example.potm.framework.config.permission.user.UserScopeEnum;
import org.example.potm.framework.config.permission.user.UserStatusEnum;
import org.example.potm.framework.pojo.Query;
import org.springdoc.core.annotations.ParameterObject;

/**
 * @author jianchengwang
 * @date 2023/4/8
 */
@Getter
@Setter
@ParameterObject
public class SysUserQuery extends Query {
    @Parameter(description = "模糊搜索")
    private String q;
    @Parameter(description = "用户归属")
    private UserScopeEnum userScope;
    @Parameter(description = "用户状态")
    private UserStatusEnum userStatus;
    @Parameter(description = "部门编号")
    private String deptId;
}
