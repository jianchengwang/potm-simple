package org.example.potm.svc.quartz.constant;

import org.example.potm.framework.config.dict.DictEnum;
import org.example.potm.framework.config.dict.DictKeyEnum;
import org.example.potm.framework.pojo.IBaseEnum;

@DictEnum(dictKey = DictKeyEnum.quartz_log_status)
public enum QuartzLogStatusEnum implements IBaseEnum<String> {
    SUCCESS("1", "成功"),
    FAIL("2", "失败");

    private String value;
    private String description;

    QuartzLogStatusEnum(String value, String description) {
        this.value = value;
        this.description = description;
    }

    @Override
    public String getValue() {
        return this.value;
    }

    @Override
    public String getDescription() {
        return this.description;
    }
}
