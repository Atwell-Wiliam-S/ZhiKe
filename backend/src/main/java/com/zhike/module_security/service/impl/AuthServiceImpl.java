package com.zhike.module_security.service.impl;

import com.zhike.common.exception.BusinessException;
import com.zhike.module_security.dto.*;
import com.zhike.module_security.service.AuthService;
import com.zhike.module_security.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import javax.annotation.PostConstruct;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // 模拟用户数据
    private final Map<String, UserInfo> USER_MAP = new HashMap<>();
    private final Map<String, Role> ROLE_MAP = new HashMap<>();

    public AuthServiceImpl() {
        // 初始化角色
        ROLE_MAP.put("ADMIN", new Role("ADMIN", "管理员", "系统管理员，拥有所有权限"));
        ROLE_MAP.put("TEACHER", new Role("TEACHER", "教师", "教师，可管理课程、视频、查看学情"));
        ROLE_MAP.put("STUDENT", new Role("STUDENT", "学生", "学生，可观看视频、提问、查看个人学情"));
    }

    @PostConstruct
    public void initUsers() {
        // 初始化用户
        USER_MAP.put("admin", new UserInfo(1L, "admin", passwordEncoder.encode("123456"), "admin@zhike.com", null, "ADMIN"));
        USER_MAP.put("teacher", new UserInfo(2L, "teacher", passwordEncoder.encode("123456"), "teacher@zhike.com", null, "TEACHER"));
        USER_MAP.put("student", new UserInfo(3L, "student", passwordEncoder.encode("123456"), "student@zhike.com", null, "STUDENT"));
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        UserInfo userInfo = USER_MAP.get(request.getUsername());
        if (userInfo == null || !passwordEncoder.matches(request.getPassword(), userInfo.getPassword())) {
            throw BusinessException.unauthorized("用户名或密码错误");
        }

        // 生成token
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userInfo.getId());
        claims.put("roleCode", userInfo.getRoleCode());
        String token = jwtUtil.generateToken(claims);

        // 构建响应
        LoginResponse response = new LoginResponse();
        response.setToken(token);

        // 构建用户信息
        UserInfoDTO userInfoDTO = new UserInfoDTO();
        userInfoDTO.setUserId(userInfo.getId());
        userInfoDTO.setUsername(userInfo.getUsername());
        userInfoDTO.setEmail(userInfo.getEmail());
        userInfoDTO.setAvatarUrl(userInfo.getAvatarUrl());
        userInfoDTO.setRoleCode(userInfo.getRoleCode());
        userInfoDTO.setRoleName(ROLE_MAP.get(userInfo.getRoleCode()).getName());
        response.setUserInfo(userInfoDTO);

        // 构建角色信息
        Role role = ROLE_MAP.get(userInfo.getRoleCode());
        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setRoleCode(role.getCode());
        roleDTO.setRoleName(role.getName());
        roleDTO.setDescription(role.getDescription());
        response.setRoles(List.of(roleDTO));

        return response;
    }

    @Override
    public Long register(RegisterRequest request) {
        if (USER_MAP.containsKey(request.getUsername())) {
            throw BusinessException.badRequest("用户名已存在");
        }

        if (!ROLE_MAP.containsKey(request.getRoleCode())) {
            throw BusinessException.badRequest("无效的角色代码");
        }

        // 创建新用户
        Long userId = (long) (USER_MAP.size() + 1);
        UserInfo userInfo = new UserInfo(
                userId,
                request.getUsername(),
                passwordEncoder.encode(request.getPassword()),
                request.getEmail(),
                null,
                request.getRoleCode()
        );
        USER_MAP.put(request.getUsername(), userInfo);

        return userId;
    }

    @Override
    public void logout() {
        // 实际项目中，需要将token加入黑名单
        // 这里简化处理
    }

    @Override
    public String refreshToken() {
        // 实际项目中，需要从当前token中获取用户信息并生成新token
        // 这里简化处理
        return "new_token";
    }

    @Override
    public Object getUserInfo() {
        // 实际项目中，需要从当前认证上下文获取用户信息
        // 这里返回模拟数据
        UserInfo userInfo = USER_MAP.get("student");
        UserInfoDTO userInfoDTO = new UserInfoDTO();
        userInfoDTO.setUserId(userInfo.getId());
        userInfoDTO.setUsername(userInfo.getUsername());
        userInfoDTO.setEmail(userInfo.getEmail());
        userInfoDTO.setAvatarUrl(userInfo.getAvatarUrl());
        userInfoDTO.setRoleCode(userInfo.getRoleCode());
        userInfoDTO.setRoleName(ROLE_MAP.get(userInfo.getRoleCode()).getName());
        return userInfoDTO;
    }

    // 内部类：模拟用户信息
    private static class UserInfo {
        private Long id;
        private String username;
        private String password;
        private String email;
        private String avatarUrl;
        private String roleCode;

        public UserInfo(Long id, String username, String password, String email, String avatarUrl, String roleCode) {
            this.id = id;
            this.username = username;
            this.password = password;
            this.email = email;
            this.avatarUrl = avatarUrl;
            this.roleCode = roleCode;
        }

        public Long getId() {
            return id;
        }

        public String getUsername() {
            return username;
        }

        public String getPassword() {
            return password;
        }

        public String getEmail() {
            return email;
        }

        public String getAvatarUrl() {
            return avatarUrl;
        }

        public String getRoleCode() {
            return roleCode;
        }
    }

    // 内部类：模拟角色信息
    private static class Role {
        private String code;
        private String name;
        private String description;

        public Role(String code, String name, String description) {
            this.code = code;
            this.name = name;
            this.description = description;
        }

        public String getCode() {
            return code;
        }

        public String getName() {
            return name;
        }

        public String getDescription() {
            return description;
        }
    }
}
