package com.zhike.module_ai_adapter.dto;

import lombok.Data;

@Data
public class TextGenerationResponse {
    private String id;
    private String object;
    private Long created;
    private String model;
    private Choice[] choices;
    private Usage usage;
}
