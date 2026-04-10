package com.zhike.module_analytics.dto;

import lombok.Data;

@Data
public class HeatPoint {
    private Double startTime;
    private Double endTime;
    private Double score;
    private Boolean isDifficulty;
}
