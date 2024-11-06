package org.example.potm.plugins.push.mail.constant;

import lombok.Getter;
import org.example.potm.framework.exception.ErrorCode;

@Getter
public enum MailErrorCode implements ErrorCode {
    SECOND_ERROR(500, "发送邮件异常"),
    ;
    private final int httpStatusCode;
    private final String message;
    MailErrorCode(int httpStatusCode, String message) {
        this.httpStatusCode = httpStatusCode;
        this.message = message;
    }

    public String getModule() {
        return "PLUGIN-PUSH-MAIL";
    }

}
