package com.zhike.module_student.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("notes")
public class Note {

    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userId;
    private Long videoId;
    private Double timestamp;
    private String content;
    private Integer isPublic;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
