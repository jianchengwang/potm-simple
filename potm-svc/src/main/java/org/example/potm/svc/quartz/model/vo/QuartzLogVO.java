package org.example.potm.svc.quartz.model.vo;

import org.example.potm.svc.quartz.constant.QuartzLogStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "定时任务日志-VO")
public class QuartzLogVO {
    @Schema(description = "编号")
    private Long id;
    @Schema(description = "定时任务编号")
    private Long jobId;
    @Schema(description = "处理类名")
    private String beanName;
    @Schema(description = "参数")
    private String params;
    @Schema(description = "日志状态：1成功，2失败")
    private QuartzLogStatusEnum quartzLogStatus;
    @Schema(description = "失败信息")
    private String error;
    @Schema(description = "耗时（毫秒）")
    private Integer times;
    @Schema(description = "创建时间")
    private LocalDateTime createTime;

}
