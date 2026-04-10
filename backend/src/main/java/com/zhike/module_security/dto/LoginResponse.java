package com.zhike.module_security.dto;

import lombok.Data;

import java.util.List;

@Data
public class LoginResponse {
    private String token;
    private UserInfoDTO userInfo;
    private List<RoleDTO> roles;
}
