package org.example.potm.svc.quartz.model.dto;

import org.example.potm.svc.quartz.constant.QuartzJobStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.potm.framework.pojo.DTO;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "定时任务-DTO")
public class QuartzJobSaveDTO implements DTO {

    @Schema(description = "任务编号")
    private Long id;

    @Schema(description = "任务处理类名称")
    @NotEmpty(message = "任务处理类名称不能为空")
    private String beanName;

    @Schema(description = "执行参数")
    private String params;

    @Schema(description = "cron表达式")
    @NotEmpty(message = "cron表达式不能为空")
    private String cronExpres;

    @Schema(description = "任务状态")
    @NotNull(message = "任务状态不能为空")
    private QuartzJobStatusEnum quartzJobStatus;

    @Schema(description = "备注")
    @NotEmpty(message = "备注不能为空")
    private String remark;
}
