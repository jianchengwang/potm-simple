package org.example.potm.framework.config.permission.user;

import org.example.potm.framework.config.dict.DictKeyEnum;
import org.example.potm.framework.config.dict.DictEnum;
import org.example.potm.framework.pojo.IBaseEnum;

/**
 * @author jianchengwang
 * @date 2023/3/31
 */
@DictEnum(dictKey = DictKeyEnum.user_scope)
public enum UserScopeEnum implements IBaseEnum<Integer> {
    ADMIN(1, "管理人员"),
    CLIENT(2, "客户端用户"),
    ;

    private Integer value;
    private String description;

    UserScopeEnum(Integer value, String description) {
        this.value = value;
        this.description = description;
    }

    @Override
    public Integer getValue() {
        return this.value;
    }

    @Override
    public Object getDescription() {
        return this.description;
    }
}
