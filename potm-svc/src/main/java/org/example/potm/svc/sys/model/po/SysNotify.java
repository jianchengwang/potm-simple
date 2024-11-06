package org.example.potm.svc.sys.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.example.potm.framework.pojo.PO;

import java.time.LocalDateTime;

/**
 * <p>
 * 系统通知
 * </p>
 *
 * @author jianchengwang
 * @since 2023-12-26
 */
@Data
@TableName("sys_notify")
public class SysNotify implements PO {
    private static final long serialVersionUID = 1L;
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    private String content;
    private String notifyType;
    private String targetId;
    private String notifyTargetType;
    private String notifyTargetAction;
    private String senderId;
    private String notifySenderType;
    private Short isRead;
    private Long userId;
    private LocalDateTime createTime;

    public SysNotify() {
    }

    public SysNotify(String content, String notifyType, String targetId, String notifyTargetType, String notifyTargetAction, String senderId, String notifySenderType, Long userId) {
        this.content = content;
        this.notifyType = notifyType;
        this.targetId = targetId;
        this.notifyTargetType = notifyTargetType;
        this.notifyTargetAction = notifyTargetAction;
        this.senderId = senderId;
        this.notifySenderType = notifySenderType;
        this.userId = userId;
    }

    public SysNotify(String content, String notifyType, String targetId, Long userId) {
        this.content = content;
        this.notifyType = notifyType;
        this.targetId = targetId;
        this.senderId = "0";
        this.userId = userId;
    }

    public SysNotify(String content, String notifyType, String targetId, String senderId, Long userId) {
        this.content = content;
        this.notifyType = notifyType;
        this.targetId = targetId;
        this.senderId = senderId;
        this.userId = userId;
    }
}
