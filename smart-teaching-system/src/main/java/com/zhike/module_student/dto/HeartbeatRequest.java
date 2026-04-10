package com.zhike.module_student.dto;

import lombok.Data;

@Data
public class HeartbeatRequest {
    private int videoId;
    private double currentTime;
    private String status;
}