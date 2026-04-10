package com.zhike.module_security.service;

import com.zhike.module_security.model.User;

public interface UserService {

    User findByUsername(String username);

    User findById(Long id);

    User register(String username, String password, String email, String roleCode, String roleName);

    boolean validatePassword(String rawPassword, String encodedPassword);
}
