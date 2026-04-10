package com.zhike.module_student.dto;

import lombok.Data;

@Data
public class NoteDTO {
    private int id;
    private int videoId;
    private double timestamp;
    private String content;
    private boolean isPublic;
    private String createdAt;
}