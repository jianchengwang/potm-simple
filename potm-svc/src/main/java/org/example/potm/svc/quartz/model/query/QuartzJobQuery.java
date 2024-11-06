package org.example.potm.svc.quartz.model.query;

import org.example.potm.svc.quartz.constant.QuartzJobStatusEnum;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.Getter;
import lombok.Setter;
import org.example.potm.framework.pojo.Query;
import org.springdoc.core.annotations.ParameterObject;

@Getter
@Setter
@ParameterObject
public class QuartzJobQuery extends Query {
    @Parameter(description = "任务状态")
    private QuartzJobStatusEnum quartzJobStatus;
}
