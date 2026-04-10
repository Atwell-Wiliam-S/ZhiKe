package com.zhike.common.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 统一响应结果类
 * 用于封装API接口的响应
 * @param <T> 数据类型
 */
public class Result<T> {

    /**
     * 业务状态码
     */
    private int code;
    
    /**
     * 状态描述
     */
    private String message;
    
    /**
     * 业务数据
     */
    private T data;
    
    /**
     * 服务器响应时间
     */
    private String timestamp;

    /**
     * 日期时间格式化器
     */
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX");

    /**
     * 私有构造函数
     * @param code 业务状态码
     * @param message 状态描述
     * @param data 业务数据
     */
    private Result(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.timestamp = LocalDateTime.now().format(FORMATTER);
    }

    /**
     * 成功响应（无数据）
     * @param <T> 数据类型
     * @return 响应对象
     */
    public static <T> Result<T> success() {
        return new Result<>(200, "操作成功", null);
    }

    /**
     * 成功响应（含数据）
     * @param data 业务数据
     * @param <T> 数据类型
     * @return 响应对象
     */
    public static <T> Result<T> success(T data) {
        return new Result<>(200, "操作成功", data);
    }

    /**
     * 错误响应（指定状态码）
     * @param code 错误码
     * @param message 错误消息
     * @param <T> 数据类型
     * @return 响应对象
     */
    public static <T> Result<T> error(int code, String message) {
        return new Result<>(code, message, null);
    }

    /**
     * 错误响应（默认状态码400）
     * @param message 错误消息
     * @param <T> 数据类型
     * @return 响应对象
     */
    public static <T> Result<T> error(String message) {
        return new Result<>(400, message, null);
    }

    /**
     * 获取业务状态码
     * @return 业务状态码
     */
    public int getCode() {
        return code;
    }

    /**
     * 设置业务状态码
     * @param code 业务状态码
     */
    public void setCode(int code) {
        this.code = code;
    }

    /**
     * 获取状态描述
     * @return 状态描述
     */
    public String getMessage() {
        return message;
    }

    /**
     * 设置状态描述
     * @param message 状态描述
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * 获取业务数据
     * @return 业务数据
     */
    public T getData() {
        return data;
    }

    /**
     * 设置业务数据
     * @param data 业务数据
     */
    public void setData(T data) {
        this.data = data;
    }

    /**
     * 获取服务器响应时间
     * @return 服务器响应时间
     */
    public String getTimestamp() {
        return timestamp;
    }

    /**
     * 设置服务器响应时间
     * @param timestamp 服务器响应时间
     */
    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

}