package org.example.potm.svc.sys.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.example.potm.framework.pojo.VO;

import java.time.LocalDateTime;
/**
* @author jianchengwang
* @since 2023-12-26
*/
@Data
@Schema(description = "管理端-系统模块-系统通知-VO")
public class SysNotifyVO implements VO {
    @Schema(description = "主键")
    private Long id;
    @Schema(description = "通知内容")
    private String content;
    @Schema(description = "通知类型")
    private String notifyType;
    @Schema(description = "通知目标编号")
    private String targetId;
    @Schema(description = "通知目标类型")
    private String notifyTargetType;
    @Schema(description = "通知触发动作")
    private String notifyTargetAction;
    @Schema(description = "通知发送者编号")
    private String senderId;
    @Schema(description = "通知发送者类型")
    private String notifySenderType;
    @Schema(description = "是否已读")
    private Short isRead;
    @Schema(description = "用户编号")
    private Long userId;
    @Schema(description = "创建时间")
    private LocalDateTime createTime;
}
