package com.zhike.service;

import com.zhike.entity.User;
import com.zhike.entity.Role;
import com.zhike.module_security.utils.JwtUtil;
import com.zhike.mapper.UserMapper;
import com.zhike.mapper.RoleMapper;
import com.zhike.mapper.UserRoleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AuthService {
    
    @Autowired
    private UserMapper userMapper;
    
    @Autowired
    private RoleMapper roleMapper;
    
    @Autowired
    private UserRoleMapper userRoleMapper;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    public User login(String username, String password) {
        User user = userMapper.selectUserWithRole(username);
        if (user == null || !passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("用户名或密码错误");
        }
        if (user.getStatus() == 0) {
            throw new RuntimeException("用户已被禁用");
        }
        return user;
    }
    
    public String generateToken(User user) {
        return jwtUtil.generateToken(user.getUsername(), user.getId(), user.getRole().getRoleCode());
    }
    
    public List<Role> getUserRoles(Integer userId) {
        List<Role> roles = new ArrayList<>();
        // 这里简化处理，实际应该通过user_roles表查询
        User user = userMapper.selectById(userId);
        if (user != null && user.getRole() != null) {
            roles.add(user.getRole());
        }
        return roles;
    }
}
