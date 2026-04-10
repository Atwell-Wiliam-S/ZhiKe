package com.zhike.module_video.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("videos")
public class Video {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer courseId;
    private String title;
    private String ossUrl;
    private Integer duration;
    private Integer parseStatus; // 0-待解析,1-进行中,2-完成,3-失败
    private String thumbnailUrl;
    private Integer orderNum;
}
