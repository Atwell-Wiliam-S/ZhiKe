package com.zhike.module_ai_adapter.dto;

import lombok.Data;

import java.util.List;

@Data
public class EmbeddingResponse {
    private String id;
    private String object;
    private Long created;
    private String model;
    private List<Embedding> data;
    private Usage usage;
}
