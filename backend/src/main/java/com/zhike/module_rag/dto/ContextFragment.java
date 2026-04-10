package com.zhike.module_rag.dto;

import lombok.Data;

@Data
public class ContextFragment {
    private Integer fragmentId;
    private Double startTime;
    private Double endTime;
    private Double relevance;
}
