package com.zhike.module_student.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 笔记模型
 * 对应数据库中的notes表
 */
@Data
@TableName("notes")
public class Note {

    /**
     * 笔记ID
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
     * 时间戳（秒）
     */
    private Double timestamp;
    
    /**
     * 笔记内容
     */
    private String content;
    
    /**
     * 是否公开：0-私密，1-公开
     */
    private Integer isPublic;
    
    /**
     * 创建时间
     */
    private LocalDateTime createdAt;
    
    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;
}
