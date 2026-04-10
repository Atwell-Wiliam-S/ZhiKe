package com.zhike.module_student.dto;

import lombok.Data;

@Data
public class VideoDTO {
    private Integer id;
    private String title;
    private Integer duration;
    private String thumbnailUrl;
    private Integer orderNum;
    private Double progress;
    private Boolean isCompleted;
}
