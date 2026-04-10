package com.zhike.module_security.controller;

import com.zhike.common.util.JwtUtil;
import com.zhike.common.util.Result;
import com.zhike.module_security.model.User;
import com.zhike.module_security.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.HashMap;
import java.util.Map;

/**
 * 认证授权控制器
 * 处理用户登录、注册、登出、刷新Token、获取用户信息等认证相关操作
 */
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserService userService;

    /**
     * 用户登录
     * @param request 登录请求参数
     * @return 登录结果，包含token、用户信息和角色信息
     */
    @PostMapping("/login")
    public Result<?> login(@Validated @RequestBody LoginRequest request) {
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

    /**
     * 用户注册
     * @param request 注册请求参数
     * @return 注册结果，返回用户ID
     */
    @PostMapping("/register")
    public Result<?> register(@Validated @RequestBody RegisterRequest request) {
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

    /**
     * 用户登出
     * @param authentication 认证信息
     * @return 登出结果
     */
    @PostMapping("/logout")
    public Result<?> logout(Authentication authentication) {
        // 这里可以实现Token失效逻辑，例如将Token加入黑名单
        return Result.success();
    }

    /**
     * 刷新Token
     * @param authentication 认证信息
     * @return 刷新结果，包含新的token和过期时间
     */
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

    /**
     * 获取用户信息
     * @param authentication 认证信息
     * @return 用户信息
     */
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

    /**
     * 登录请求参数
     */
    public static class LoginRequest {
        @NotBlank(message = "用户名不能为空")
        private String username;
        @NotBlank(message = "密码不能为空")
        private String password;

        /**
         * 获取用户名
         * @return 用户名
         */
        public String getUsername() {
            return username;
        }

        /**
         * 设置用户名
         * @param username 用户名
         */
        public void setUsername(String username) {
            this.username = username;
        }

        /**
         * 获取密码
         * @return 密码
         */
        public String getPassword() {
            return password;
        }

        /**
         * 设置密码
         * @param password 密码
         */
        public void setPassword(String password) {
            this.password = password;
        }
    }

    /**
     * 注册请求参数
     */
    public static class RegisterRequest {
        @NotBlank(message = "用户名不能为空")
        @Size(min = 3, max = 50, message = "用户名长度必须在3-50之间")
        private String username;
        @NotBlank(message = "密码不能为空")
        @Size(min = 6, max = 100, message = "密码长度必须在6-100之间")
        private String password;
        @NotBlank(message = "角色代码不能为空")
        private String roleCode;
        @Email(message = "邮箱格式不正确")
        private String email;

        /**
         * 获取用户名
         * @return 用户名
         */
        public String getUsername() {
            return username;
        }

        /**
         * 设置用户名
         * @param username 用户名
         */
        public void setUsername(String username) {
            this.username = username;
        }

        /**
         * 获取密码
         * @return 密码
         */
        public String getPassword() {
            return password;
        }

        /**
         * 设置密码
         * @param password 密码
         */
        public void setPassword(String password) {
            this.password = password;
        }

        /**
         * 获取角色代码
         * @return 角色代码
         */
        public String getRoleCode() {
            return roleCode;
        }

        /**
         * 设置角色代码
         * @param roleCode 角色代码
         */
        public void setRoleCode(String roleCode) {
            this.roleCode = roleCode;
        }

        /**
         * 获取邮箱
         * @return 邮箱
         */
        public String getEmail() {
            return email;
        }

        /**
         * 设置邮箱
         * @param email 邮箱
         */
        public void setEmail(String email) {
            this.email = email;
        }
    }

}