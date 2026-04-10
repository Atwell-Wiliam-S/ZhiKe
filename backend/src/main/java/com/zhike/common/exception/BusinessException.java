package com.zhike.common.exception;

/**
 * 业务异常类
 * 用于处理业务逻辑中的异常情况
 */
public class BusinessException extends RuntimeException {

    /**
     * 错误码
     */
    private int code;

    /**
     * 构造函数
     * @param code 错误码
     * @param message 错误消息
     */
    public BusinessException(int code, String message) {
        super(message);
        this.code = code;
    }

    /**
     * 构造函数（默认错误码400）
     * @param message 错误消息
     */
    public BusinessException(String message) {
        super(message);
        this.code = 400;
    }

    /**
     * 获取错误码
     * @return 错误码
     */
    public int getCode() {
        return code;
    }

    /**
     * 设置错误码
     * @param code 错误码
     */
    public void setCode(int code) {
        this.code = code;
    }

}