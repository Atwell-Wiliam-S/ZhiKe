package com.zhike.module_student.dto;

import lombok.Data;

import java.util.List;

@Data
public class CourseDetailDTO {
    private Integer id;
    private String title;
    private String description;
    private String coverImage;
    private String category;
    private String teacherName;
    private List<VideoDTO> videos;
}
