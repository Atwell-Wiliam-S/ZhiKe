package com.zhike.module_ai_adapter.dto;

import lombok.Data;

@Data
public class Message {
    private String role; // system, user, assistant
    private String content;
}
