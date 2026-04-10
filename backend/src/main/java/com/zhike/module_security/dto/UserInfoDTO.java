package com.zhike.module_security.dto;

import lombok.Data;

@Data
public class UserInfoDTO {
    private Long userId;
    private String username;
    private String email;
    private String avatarUrl;
    private String roleCode;
    private String roleName;
}
