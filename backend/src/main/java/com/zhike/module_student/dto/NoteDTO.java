package com.zhike.module_student.dto;

import lombok.Data;

@Data
public class NoteDTO {
    private Integer id;
    private Integer videoId;
    private Double timestamp;
    private String content;
    private String createdAt;
}
