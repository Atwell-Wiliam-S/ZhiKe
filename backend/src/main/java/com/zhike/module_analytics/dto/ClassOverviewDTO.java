package com.zhike.module_analytics.dto;

import lombok.Data;

@Data
public class ClassOverviewDTO {
    private Integer totalStudents;
    private Double avgCompletionRate;
    private Integer avgWatchDuration;
    private Integer activeTodayCount;
    private Integer totalQuestions;
}
