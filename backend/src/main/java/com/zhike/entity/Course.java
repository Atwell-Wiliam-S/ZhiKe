package com.zhike.entity;

import lombok.Data;

import java.util.Date;

@Data
public class Course {
    private Integer id;
    private String title;
    private Integer teacherId;
    private String description;
    private String coverImage;
    private String category;
    private Integer isPublished;
    private Date createdAt;
    private User teacher;
}
