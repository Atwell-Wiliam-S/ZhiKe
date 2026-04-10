package com.zhike.module_security.controller;

import com.zhike.common.util.JwtUtil;
import com.zhike.common.util.Result;
import com.zhike.module_security.model.User;
import com.zhike.module_security.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
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
    private UserService userService;

    @PostMapping("/login")
    public Result<?> login(@RequestBody LoginRequest request) {
        User user = userService.findByUsername(request.getUsername());
        if (user == null || !userService.validatePassword(request.getPassword(), user.getPassword())) {
            return Result.error(401, "用户名或密码错误");
        }

        String token = jwtUtil.generateToken(user.getId().toString(), user.getRoleCode());
        Map<String, Object> data = new HashMap<>();
        data.put("token", token);
        data.put("userInfo", Map.of(
                "userId", user.getId(),
                "username", user.getUsername(),
                "email", user.getEmail(),
                "avatarUrl", user.getAvatarUrl(),
                "roleCode", user.getRoleCode(),
                "roleName", user.getRoleName()
        ));
        data.put("roles", Map.of(
                "roleCode", user.getRoleCode(),
                "roleName", user.getRoleName(),
                "description", ""
        ));

        return Result.success(data);
    }

    @PostMapping("/register")
    public Result<?> register(@RequestBody RegisterRequest request) {
        User existingUser = userService.findByUsername(request.getUsername());
        if (existingUser != null) {
            return Result.error(400, "用户名已存在");
        }

        User user = userService.register(
                request.getUsername(),
                request.getPassword(),
                request.getEmail(),
                request.getRoleCode(),
                request.getRoleCode().equals("STUDENT") ? "学生" : "教师"
        );

        return Result.success(user.getId());
    }

    @PostMapping("/logout")
    public Result<?> logout(Authentication authentication) {
        // 这里可以实现Token失效逻辑，例如将Token加入黑名单
        return Result.success();
    }

    @PostMapping("/refresh")
    public Result<?> refresh(Authentication authentication) {
        // 从认证信息中获取用户ID和角色
        String userId = authentication.getName();
        User user = userService.findById(Long.valueOf(userId));
        if (user == null) {
            return Result.error(401, "用户不存在");
        }

        String newToken = jwtUtil.generateToken(userId, user.getRoleCode());
        Map<String, Object> data = new HashMap<>();
        data.put("token", newToken);
        data.put("expiresIn", 86400); // 24小时

        return Result.success(data);
    }

    @GetMapping("/info")
    public Result<?> getInfo(Authentication authentication) {
        String userId = authentication.getName();
        User user = userService.findById(Long.valueOf(userId));
        if (user == null) {
            return Result.error(401, "用户不存在");
        }

        Map<String, Object> userInfo = Map.of(
                "userId", user.getId(),
                "username", user.getUsername(),
                "email", user.getEmail(),
                "avatarUrl", user.getAvatarUrl(),
                "roleCode", user.getRoleCode(),
                "roleName", user.getRoleName()
        );

        return Result.success(userInfo);
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
        private String roleCode;
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

        public String getRoleCode() {
            return roleCode;
        }

        public void setRoleCode(String roleCode) {
            this.roleCode = roleCode;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }
    }

}