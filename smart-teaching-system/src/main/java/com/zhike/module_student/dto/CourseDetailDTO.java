package com.zhike.module_student.dto;

import lombok.Data;
import java.util.List;

@Data
public class CourseDetailDTO {
    private int id;
    private String title;
    private String teacherName;
    private String description;
    private String coverImage;
    private List<VideoDTO> videos;
}