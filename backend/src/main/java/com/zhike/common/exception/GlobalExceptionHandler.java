package com.zhike.common.exception;

import com.zhike.common.result.Result;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.sql.SQLException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 处理业务异常
    @ExceptionHandler(BusinessException.class)
    public Result<?> handleBusinessException(BusinessException e) {
        return Result.error(e.getCode(), e.getMessage());
    }

    // 处理参数验证异常
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<?> handleValidationException(MethodArgumentNotValidException e) {
        String message = e.getBindingResult().getFieldError().getDefaultMessage();
        return Result.badRequest(message);
    }

    // 处理404异常
    @ExceptionHandler(NoHandlerFoundException.class)
    public Result<?> handleNotFoundException(NoHandlerFoundException e) {
        return Result.notFound("接口不存在");
    }

    // 处理SQL异常
    @ExceptionHandler(SQLException.class)
    public Result<?> handleSQLException(SQLException e) {
        return Result.error("数据库操作异常: " + e.getMessage());
    }

    // 处理IO异常
    @ExceptionHandler(IOException.class)
    public Result<?> handleIOException(IOException e) {
        return Result.error("IO操作异常: " + e.getMessage());
    }

    // 处理其他所有异常
    @ExceptionHandler(Exception.class)
    public Result<?> handleException(Exception e, HttpServletRequest request) {
        e.printStackTrace();
        return Result.error("服务器内部错误: " + e.getMessage());
    }
}
