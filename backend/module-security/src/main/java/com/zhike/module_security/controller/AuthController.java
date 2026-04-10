package com.zhike.module_security.controller;

import com.zhike.common.util.JwtUtil;
import com.zhike.common.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public Result<?> login(@RequestBody LoginRequest request) {
        // 这里应该从数据库查询用户信息，验证用户名和密码
        // 为了演示，这里使用硬编码的用户信息
        if ("admin".equals(request.getUsername()) && "admin123".equals(request.getPassword())) {
            String token = jwtUtil.generateToken("1", "ADMIN");
            Map<String, Object> data = new HashMap<>();
            data.put("token", token);
            data.put("userInfo", Map.of(
                    "id", 1,
                    "username", "admin",
                    "roleCode", "ADMIN"
            ));
            return Result.success(data);
        }
        return Result.error(401, "用户名或密码错误");
    }

    @PostMapping("/register")
    public Result<?> register(@RequestBody RegisterRequest request) {
        // 这里应该将用户信息保存到数据库
        // 为了演示，这里直接返回成功
        String encodedPassword = passwordEncoder.encode(request.getPassword());
        return Result.success("注册成功");
    }

    public static class LoginRequest {
        private String username;
        private String password;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }

    public static class RegisterRequest {
        private String username;
        private String password;
        private String email;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }
    }

}