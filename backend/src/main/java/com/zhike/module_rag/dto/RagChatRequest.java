package com.zhike.module_rag.dto;

import lombok.Data;

@Data
public class RagChatRequest {
    private String question;
    private Integer videoId;
    private Double currentTime;
}
