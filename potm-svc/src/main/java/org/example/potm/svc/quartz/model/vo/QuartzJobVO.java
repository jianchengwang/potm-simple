package org.example.potm.svc.quartz.model.vo;

import org.example.potm.svc.quartz.constant.QuartzJobStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.potm.framework.pojo.VO;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "定时任务-VO")
public class QuartzJobVO implements VO {
    private static final long serialVersionUID = 1L;

    @Schema(description = "任务编号")
    private Long id;

    @Schema(description = "Bean名称")
    private String beanName;

    @Schema(description = "执行参数")
    private String params;

    @Schema(description = "Cron表达式")
    private String cronExpres;

    @Schema(description = "任务状态：1启用，2禁用")
    private QuartzJobStatusEnum quartzJobStatus;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;
}
