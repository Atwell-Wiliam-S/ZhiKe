package com.zhike.module_student.dto;

import lombok.Data;

@Data
public class ProgressRequest {
    private int videoId;
    private double currentTime;
    private int duration;
}