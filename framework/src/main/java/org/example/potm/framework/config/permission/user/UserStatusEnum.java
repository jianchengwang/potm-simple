package org.example.potm.framework.config.permission.user;

import org.example.potm.framework.config.dict.DictKeyEnum;
import org.example.potm.framework.config.dict.DictEnum;
import org.example.potm.framework.pojo.IBaseEnum;

/**
 * @author jianchengwang
 * @date 2023/3/31
 */
@DictEnum(dictKey = DictKeyEnum.user_status)
public enum UserStatusEnum implements IBaseEnum<Integer> {
    /**
     * 正常
     */
    ENABLE(1, "启用"),
    /**
     * 注销
     */
    DISABLE(2, "禁用");

    private Integer value;
    private String description;

    UserStatusEnum(Integer value, String description) {
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
