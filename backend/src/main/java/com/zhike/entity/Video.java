package com.zhike.entity;

import lombok.Data;

@Data
public class Video {
    private Integer id;
    private Integer courseId;
    private String title;
    private String ossUrl;
    private Integer duration;
    private Integer parseStatus;
    private String thumbnailUrl;
    private Integer orderNum;
    private Course course;
}
