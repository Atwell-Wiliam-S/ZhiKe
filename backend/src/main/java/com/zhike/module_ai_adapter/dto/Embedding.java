package com.zhike.module_ai_adapter.dto;

import lombok.Data;

import java.util.List;

@Data
public class Embedding {
    private Integer index;
    private List<Double> embedding;
    private String object;
}
