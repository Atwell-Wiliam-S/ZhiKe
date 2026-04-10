package com.zhike.module_security.service.impl;

import com.zhike.module_security.mapper.UserMapper;
import com.zhike.module_security.model.User;
import com.zhike.module_security.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * 用户服务实现类
 * 实现用户相关的业务逻辑操作
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * 根据用户名查找用户
     * @param username 用户名
     * @return 用户对象
     */
    @Override
    public User findByUsername(String username) {
        return userMapper.findByUsername(username);
    }

    /**
     * 根据ID查找用户
     * @param id 用户ID
     * @return 用户对象
     */
    @Override
    public User findById(Long id) {
        return userMapper.findById(id);
    }

    /**
     * 用户注册
     * @param username 用户名
     * @param password 密码
     * @param email 邮箱
     * @param roleCode 角色代码
     * @param roleName 角色名称
     * @return 注册的用户对象
     */
    @Override
    public User register(String username, String password, String email, String roleCode, String roleName) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setEmail(email);
        user.setRoleCode(roleCode);
        user.setRoleName(roleName);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        userMapper.insert(user);
        return user;
    }

    /**
     * 验证密码
     * @param rawPassword 原始密码
     * @param encodedPassword 加密后的密码
     * @return 是否验证通过
     */
    @Override
    public boolean validatePassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
}
