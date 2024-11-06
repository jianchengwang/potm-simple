package org.example.potm.plugins.push.sms.constant;

import lombok.Getter;
import org.example.potm.framework.exception.ErrorCode;

@Getter
public enum SMSErrorCode implements ErrorCode {
    SECOND_SMS_ERROR(400, "发送短信异常"),
    CONFIG_ERROR(500, "配置信息不完整"),
    CONFIG_TEMPLATE_LIST_EMPTY(500, "配置参数templateList不能为空"),
    TEMPLATE_ID_NOT_FOUND(400, "模板编号不存在"),
    ;
    private final int httpStatusCode;
    private final String message;
    SMSErrorCode(int httpStatusCode, String message) {
        this.httpStatusCode = httpStatusCode;
        this.message = message;
    }

    public String getModule() {
        return "PLUGINS-PUSH-SMS";
    }

}
