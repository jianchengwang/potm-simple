package org.example.potm.svc.quartz.constant;

import org.example.potm.framework.config.dict.DictEnum;
import org.example.potm.framework.config.dict.DictKeyEnum;
import org.example.potm.framework.pojo.IBaseEnum;

@DictEnum(dictKey = DictKeyEnum.quartz_job_status)
public enum QuartzJobStatusEnum implements IBaseEnum<String> {
    RUNNING("1", "启动"),
    STOP("2", "停止"),
    ;

    private String value;
    private String description;

    QuartzJobStatusEnum(String value, String description) {
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
