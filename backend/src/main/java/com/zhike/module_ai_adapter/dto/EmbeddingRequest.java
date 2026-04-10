package com.zhike.module_ai_adapter.dto;

import lombok.Data;

import java.util.List;

@Data
public class EmbeddingRequest {
    private String model;
    private List<String> input;
    private String encodingFormat;
}
