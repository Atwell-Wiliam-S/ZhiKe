package com.zhike.module_rag.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("qa_logs")
public class QaLog {

    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userId;
    private Long videoId;
    private String question;
    private String answer;
    private String contextFragments;
    private String modelVersion;
    private Integer tokenUsage;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
