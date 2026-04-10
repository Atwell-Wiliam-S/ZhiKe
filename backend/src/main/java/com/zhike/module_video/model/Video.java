package com.zhike.module_video.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("videos")
public class Video {

    @TableId(type = IdType.AUTO)
    private Long id;
    private Long courseId;
    private String title;
    private String ossUrl;
    private Integer duration;
    private Integer parseStatus;
    private String thumbnailUrl;
    private Integer orderNum;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
