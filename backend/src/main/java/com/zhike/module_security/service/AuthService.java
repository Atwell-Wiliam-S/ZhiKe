package com.zhike.module_security.service;

import com.zhike.module_security.dto.LoginRequest;
import com.zhike.module_security.dto.LoginResponse;
import com.zhike.module_security.dto.RegisterRequest;

public interface AuthService {
    LoginResponse login(LoginRequest request);
    Long register(RegisterRequest request);
    void logout();
    String refreshToken();
    Object getUserInfo();
}
