package com.zhike.module_student.dto;

import lombok.Data;

@Data
public class VideoDTO {
    private int id;
    private int courseId;
    private String title;
    private String ossUrl;
    private int duration;
    private int parseStatus;
    private String parseStatusDesc;
    private String thumbnailUrl;
    private int orderNum;
    private String createdAt;
}