package com.zhike.module_student.dto;

import lombok.Data;

@Data
public class NoteRequest {
    private int videoId;
    private double timestamp;
    private String content;
}