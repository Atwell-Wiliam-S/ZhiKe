package com.zhike.module_analytics.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("learning_records")
public class LearningRecord {

    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userId;
    private Long videoId;
    private Double lastWatchTime;
    private Integer totalDuration;
    private Double completionRate;
    private Integer status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
