package com.zhike.module_video.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("knowledge_fragments")
public class KnowledgeFragment {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer videoId;
    private Double startTime;
    private Double endTime;
    private String contentText;
    private String keyframeUrl;
    private String asrText;
    private String visualTags;
    private String summary;
    private String keywords;
    private String vectorId;
}
