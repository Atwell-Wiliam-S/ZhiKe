package com.zhike.common.result;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Result<T> {
    private int code;
    private String message;
    private T data;
    private LocalDateTime timestamp;

    private Result(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.timestamp = LocalDateTime.now();
    }

    // 成功响应
    public static <T> Result<T> success(T data) {
        return new Result<>(200, "操作成功", data);
    }

    public static <T> Result<T> success() {
        return success(null);
    }

    // 失败响应
    public static <T> Result<T> error(int code, String message) {
        return new Result<>(code, message, null);
    }

    public static <T> Result<T> error(String message) {
        return error(500, message);
    }

    // 常见错误响应
    public static <T> Result<T> badRequest(String message) {
        return error(400, message);
    }

    public static <T> Result<T> unauthorized(String message) {
        return error(401, message);
    }

    public static <T> Result<T> forbidden(String message) {
        return error(403, message);
    }

    public static <T> Result<T> notFound(String message) {
        return error(404, message);
    }

    public static <T> Result<T> conflict(String message) {
        return error(409, message);
    }

    public static <T> Result<T> tooManyRequests(String message) {
        return error(429, message);
    }

    public static <T> Result<T> serviceUnavailable(String message) {
        return error(503, message);
    }
}
