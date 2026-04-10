package com.zhike.module_ai_adapter.dto;

import lombok.Data;

@Data
public class SpeechToTextResponse {
    private String id;
    private String object;
    private Long created;
    private String model;
    private String text;
    private Word[] words;
}
