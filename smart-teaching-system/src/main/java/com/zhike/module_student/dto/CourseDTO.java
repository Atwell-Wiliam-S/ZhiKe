package com.zhike.module_student.dto;

import lombok.Data;

@Data
public class CourseDTO {
    private int id;
    private String title;
    private String teacherName;
    private String description;
    private String coverImage;
    private int videoCount;
    private String publishedAt;
}