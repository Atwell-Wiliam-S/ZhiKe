package com.zhike.module_student.dto;

import lombok.Data;

@Data
public class NoteRequest {
    private Integer videoId;
    private Double timestamp;
    private String content;
}
