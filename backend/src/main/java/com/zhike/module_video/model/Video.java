package com.zhike.module_video.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 视频模型
 * 对应数据库中的videos表
 */
@Data
@TableName("videos")
public class Video {

    /**
     * 视频ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    
    /**
     * 课程ID
     */
    private Long courseId;
    
    /**
     * 视频标题
     */
    private String title;
    
    /**
     * OSS存储地址
     */
    private String ossUrl;
    
    /**
     * 视频时长（秒）
     */
    private Integer duration;
    
    /**
     * 解析状态：0-未解析，1-解析中，2-解析完成，3-解析失败
     */
    private Integer parseStatus;
    
    /**
     * 缩略图URL
     */
    private String thumbnailUrl;
    
    /**
     * 排序序号
     */
    private Integer orderNum;
    
    /**
     * 创建时间
     */
    private LocalDateTime createdAt;
    
    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;
}
