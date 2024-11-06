package org.example.potm.plugins.storage.constant;

import org.example.potm.framework.exception.ErrorCode;

public enum StorageErrorCode implements ErrorCode {
    PUT_OBJECT_KEY_EXISTED(400, "对象key已经存在，如果你想强制替换，请求参数forcedPut设置为true"),
    OBJECT_KEY_NOT_EXIST(400, "对象key不存在"),
    ;
    private final int httpStatusCode;
    private final String message;
    StorageErrorCode(int httpStatusCode, String message) {
        this.httpStatusCode = httpStatusCode;
        this.message = message;
    }

    @Override
    public String getModule() {
        return "PLUGINS-STORAGE";
    }

    @Override
    public int getHttpStatusCode() {
        return this.httpStatusCode;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}
