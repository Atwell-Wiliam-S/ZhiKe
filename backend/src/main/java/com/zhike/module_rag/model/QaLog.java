package com.zhike.module_rag.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 问答日志模型
 * 对应数据库中的qa_logs表
 */
@Data
@TableName("qa_logs")
public class QaLog {

    /**
     * 日志ID
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
     * 问题
     */
    private String question;
    
    /**
     * 回答
     */
    private String answer;
    
    /**
     * 上下文片段（JSON格式）
     */
    private String contextFragments;
    
    /**
     * 模型版本
     */
    private String modelVersion;
    
    /**
     * Token使用量
     */
    private Integer tokenUsage;
    
    /**
     * 创建时间
     */
    private LocalDateTime createdAt;
    
    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;
}
