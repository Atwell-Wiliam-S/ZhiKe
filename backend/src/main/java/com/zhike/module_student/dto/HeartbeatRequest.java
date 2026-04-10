package com.zhike.module_student.dto;

import lombok.Data;

@Data
public class HeartbeatRequest {
    private Integer videoId;
    private Double currentTime;
    private String status; // PLAYING, PAUSED, SEEKING
}
