package org.example.potm.framework.pojo;

import cn.hutool.core.lang.UUID;
import org.example.potm.framework.exception.ClientException;
import org.example.potm.framework.exception.ServerException;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

/**
 * @author jianchengwang
 * @date 2023/3/30
 */
@Data
@Slf4j
public class Response<T> {

    private Boolean success = true;

    /**
     * HTTP Status
     */
    private int status;

    /**
     * 错误码
     */
    private String code;

    /**
     * 错误信息
     */
    private String message;

    /**
     * 响应数据
     */
    private T data;

    private Map<String, Object> extMap;

    /**
     * 返回成功结果
     * @return 本实例
     */
    public static <T> Response<T> ok() {
        return ok(null);
    }

    /**
     * 返回成功结果
     * @param data 数据
     * @return 本实例
     */
    public static <T> Response<T> ok(T data) {
        Response<T> response = new Response<T>();
        response.setSuccess(true);
        response.setStatus(HttpStatus.OK.value());
        response.setCode(HttpStatus.OK.getReasonPhrase());
        response.setData(data);
        return response;
    }

    public static <T> Response<T> ok(T data, Map<String, Object> extMap) {
        Response<T> response = new Response<T>();
        response.setSuccess(true);
        response.setStatus(HttpStatus.OK.value());
        response.setCode(HttpStatus.OK.getReasonPhrase());
        response.setData(data);
        response.setExtMap(extMap);
        return response;
    }

    /**
     * 返回失败结果
     * @param throwable 异常信息
     * @return 本实例
     */
    public static <T> Response<T> fail(Throwable throwable) {
        if (throwable instanceof ClientException) {
            return fail((ClientException) throwable);
        }
        else if (throwable instanceof ServerException) {
            return fail((ServerException) throwable);
        }

        return fail(throwable, String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()));
    }

    /**
     * 客户端错误
     * @param code 异常标识
     * @param message 信息
     * @param data 数据
     * @param <T> 类型
     * @return 异常包裹
     */
    public static <T> Response<T> fail400(String code, String message, T data) {
        Response<T> response = new Response<T>();
        response.setSuccess(false);
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.setCode(code);
        response.setData(data);
        response.setMessage(message);
        return response;
    }

    private static <T> Response<T> fail(ClientException ex) {
        Response<T> response = new Response<T>();
        response.setSuccess(false);
        response.setStatus(ex.getErrorCode().getHttpStatusCode());
        response.setCode(ex.getCode());
        response.setMessage(ex.getMessage() != null ? ex.getMessage() : ex.getErrorCode().getMessage());
        return response;
    }

    private static <T> Response<T> fail(ServerException ex) {
        return fail(ex, ex.getCode());
    }

    private static <T> Response<T> fail(Throwable ex, String code) {
        String key = UUID.randomUUID().toString().replace("-", "") + " "
                + LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME);
        log.error(key, ex);
        Response<T> response = new Response<T>();
        response.setSuccess(false);
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        response.setCode(code);
        response.setMessage(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase() + ": " + key);
        return response;
    }

    public static <T> Response<T> fail(String code, String message) {
        String key = UUID.randomUUID().toString().replace("-", "") + " "
                + LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME);
        log.error(key, message);
        Response<T> response = new Response<T>();
        response.setSuccess(false);
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        response.setCode(code);
        response.setMessage(message!=null ? message : HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase() + ": " + key);
        return response;
    }

}

