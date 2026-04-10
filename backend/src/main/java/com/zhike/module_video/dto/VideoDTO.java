package com.zhike.module_video.dto;

import lombok.Data;

@Data
public class VideoDTO {
    private Integer id;
    private Integer courseId;
    private String title;
    private Integer duration;
    private Integer parseStatus;
    private String thumbnailUrl;
    private Integer orderNum;
    private String createdAt;
}
