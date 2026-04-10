package com.zhike.module_security.controller;

import com.zhike.common.result.Result;
import com.zhike.module_security.dto.LoginRequest;
import com.zhike.module_security.dto.LoginResponse;
import com.zhike.module_security.dto.RegisterRequest;
import com.zhike.module_security.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    // 登录
    @PostMapping("/login")
    public Result<LoginResponse> login(@RequestBody LoginRequest request) {
        LoginResponse response = authService.login(request);
        return Result.success(response);
    }

    // 注册
    @PostMapping("/register")
    public Result<Long> register(@RequestBody RegisterRequest request) {
        Long userId = authService.register(request);
        return Result.success(userId);
    }

    // 登出
    @PostMapping("/logout")
    public Result<Void> logout() {
        authService.logout();
        return Result.success();
    }

    // 刷新token
    @PostMapping("/refresh")
    public Result<String> refreshToken() {
        String newToken = authService.refreshToken();
        return Result.success(newToken);
    }

    // 获取用户信息
    @GetMapping("/info")
    public Result<?> getUserInfo() {
        Object userInfo = authService.getUserInfo();
        return Result.success(userInfo);
    }
}
