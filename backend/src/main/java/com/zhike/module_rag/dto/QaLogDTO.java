package com.zhike.module_rag.dto;

import lombok.Data;

import java.util.List;

@Data
public class QaLogDTO {
    private Integer id;
    private String question;
    private String answer;
    private List<ContextFragment> contextFragments;
    private String modelVersion;
    private Integer tokenUsage;
    private String createdAt;
}
