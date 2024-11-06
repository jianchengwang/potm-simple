package org.example.potm.svc.sys.model.query;

import io.swagger.v3.oas.annotations.Parameter;
import lombok.Getter;
import lombok.Setter;
import org.example.potm.framework.pojo.Query;
import org.springdoc.core.annotations.ParameterObject;
/**
* @author jianchengwang
* @since 2023-12-26
*/
@Getter
@Setter
@ParameterObject
public class SysNotifyQuery extends Query {
    @Parameter(description = "用户编号")
    private Long userId;
    @Parameter(description = "发送者编号")
    private String senderId;
    @Parameter(description = "是否已读")
    private Short isRead;
}
