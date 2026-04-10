package com.zhike.module_ai_adapter.dto;

import lombok.Data;

@Data
public class Choice {
    private Integer index;
    private Message message;
    private String finishReason;
}
