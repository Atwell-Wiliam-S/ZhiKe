package com.zhike.module_ai_adapter.dto;

import lombok.Data;

@Data
public class VisualAnalysisRequest {
    private String model;
    private String imageUrl;
    private String prompt;
}
