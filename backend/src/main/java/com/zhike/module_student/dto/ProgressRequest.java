package com.zhike.module_student.dto;

import lombok.Data;

@Data
public class ProgressRequest {
    private Integer videoId;
    private Double currentTime;
    private Integer duration;
}
