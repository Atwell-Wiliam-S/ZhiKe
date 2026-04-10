package com.zhike.module_ai_adapter.dto;

import lombok.Data;

import java.util.List;

@Data
public class TextGenerationRequest {
    private String model;
    private String prompt;
    private List<Message> messages;
    private Integer maxTokens;
    private Double temperature;
    private Boolean stream;
}
