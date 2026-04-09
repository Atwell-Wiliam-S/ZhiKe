package com.zhike.entity;

import lombok.Data;

@Data
public class KnowledgeFragment {
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
    private Video video;
}
