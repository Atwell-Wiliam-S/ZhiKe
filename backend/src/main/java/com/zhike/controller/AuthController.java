package com.zhike.controller;

import com.zhike.common.Result;
import com.zhike.entity.User;
import com.zhike.entity.Role;
import com.zhike.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {
    
    @Autowired
    private AuthService authService;
    
    @PostMapping("/login")
    public Result<?> login(@RequestBody Map<String, String> request) {
        String username = request.get("username");
        String password = request.get("password");
        
        User user = authService.login(username, password);
        String token = authService.generateToken(user);
        List<Role> roles = authService.getUserRoles(user.getId());
        
        return Result.success(Map.of(
            "token", token,
            "userInfo", Map.of(
                "userId", user.getId(),
                "username", user.getUsername(),
                "email", user.getEmail(),
                "avatarUrl", user.getAvatarUrl(),
                "roleCode", user.getRole().getRoleCode(),
                "roleName", user.getRole().getRoleName()
            ),
            "roles", roles
        ));
    }
    
    @PostMapping("/logout")
    public Result<?> logout() {
        // 这里可以添加token黑名单处理
        return Result.success();
    }
    
    @GetMapping("/info")
    public Result<?> getInfo() {
        // 这里需要从SecurityContext中获取当前用户信息
        return Result.success();
    }
}
