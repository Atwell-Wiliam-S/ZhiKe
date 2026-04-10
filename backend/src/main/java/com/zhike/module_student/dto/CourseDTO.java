package com.zhike.module_student.dto;

import lombok.Data;

@Data
public class CourseDTO {
    private Integer id;
    private String title;
    private String description;
    private String coverImage;
    private String category;
    private String teacherName;
    private Integer totalVideos;
    private Double completionRate;
}
