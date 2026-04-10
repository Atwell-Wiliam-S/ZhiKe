package com.zhike.module_ai_adapter.dto;

import lombok.Data;

@Data
public class VisualAnalysisResponse {
    private String id;
    private String object;
    private Long created;
    private String model;
    private String analysisResult;
    private String[] tags;
}
