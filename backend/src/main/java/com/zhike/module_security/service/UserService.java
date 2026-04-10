package com.zhike.module_security.service;

import com.zhike.module_security.model.User;

/**
 * 用户服务接口
 * 提供用户相关的业务逻辑操作
 */
public interface UserService {

    /**
     * 根据用户名查找用户
     * @param username 用户名
     * @return 用户对象
     */
    User findByUsername(String username);

    /**
     * 根据ID查找用户
     * @param id 用户ID
     * @return 用户对象
     */
    User findById(Long id);

    /**
     * 用户注册
     * @param username 用户名
     * @param password 密码
     * @param email 邮箱
     * @param roleCode 角色代码
     * @param roleName 角色名称
     * @return 注册的用户对象
     */
    User register(String username, String password, String email, String roleCode, String roleName);

    /**
     * 验证密码
     * @param rawPassword 原始密码
     * @param encodedPassword 加密后的密码
     * @return 是否验证通过
     */
    boolean validatePassword(String rawPassword, String encodedPassword);
}
