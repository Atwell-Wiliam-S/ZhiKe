package com.zhike.module_security.dto;

import lombok.Data;

@Data
public class RegisterRequest {
    private String username;
    private String password;
    private String roleCode;
    private String email;
}
