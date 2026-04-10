package com.zhike.module_ai_adapter.dto;

import lombok.Data;

@Data
public class SpeechToTextRequest {
    private String model;
    private String audioUrl;
    private String language;
}
