package com.zhike.module_analytics.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 学习记录模型
 * 对应数据库中的learning_records表
 */
@Data
@TableName("learning_records")
public class LearningRecord {

    /**
     * 记录ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    
    /**
     * 用户ID
     */
    private Long userId;
    
    /**
     * 视频ID
     */
    private Long videoId;
    
    /**
     * 最后观看时间
     */
    private Double lastWatchTime;
    
    /**
     * 总观看时长
     */
    private Integer totalDuration;
    
    /**
     * 完成率
     */
    private Double completionRate;
    
    /**
     * 状态
     */
    private Integer status;
    
    /**
     * 创建时间
     */
    private LocalDateTime createdAt;
    
    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;
}
