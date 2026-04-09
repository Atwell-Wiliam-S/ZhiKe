package com.zhike.entity;

import lombok.Data;

import java.util.Date;

@Data
public class User {
    private Integer id;
    private String username;
    private String password;
    private String email;
    private String avatarUrl;
    private Integer roleId;
    private Integer status;
    private Date createdAt;
    private Date updatedAt;
    private Role role;
}
