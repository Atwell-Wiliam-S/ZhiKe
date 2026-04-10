package com.zhike.module_video.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 知识片段模型
 * 对应数据库中的knowledge_fragments表
 */
@Data
@TableName("knowledge_fragments")
public class KnowledgeFragment {

    /**
     * 片段ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    
    /**
     * 视频ID
     */
    private Long videoId;
    
    /**
     * 起始时间（秒）
     */
    private Double startTime;
    
    /**
     * 结束时间（秒）
     */
    private Double endTime;
    
    /**
     * 融合文本
     */
    private String contentText;
    
    /**
     * 关键帧图片URL
     */
    private String keyframeUrl;
    
    /**
     * 原始ASR文本
     */
    private String asrText;
    
    /**
     * 视觉标签（JSON格式）
     */
    private String visualTags;
    
    /**
     * 摘要
     */
    private String summary;
    
    /**
     * 关键词
     */
    private String keywords;
    
    /**
     * 向量ID
     */
    private String vectorId;
    
    /**
     * 创建时间
     */
    private LocalDateTime createdAt;
    
    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;
}
