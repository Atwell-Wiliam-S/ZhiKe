package com.zhike.module_analytics.dto;

import lombok.Data;

import java.util.List;

@Data
public class StudentProfileDTO {
    private Integer studentId;
    private String studentName;
    private Double overallCompletionRate;
    private Integer totalWatchDuration;
    private Integer totalQuestions;
    private List<CoursePerformance> coursePerformances;
    private List<WeakArea> weakAreas;
}
