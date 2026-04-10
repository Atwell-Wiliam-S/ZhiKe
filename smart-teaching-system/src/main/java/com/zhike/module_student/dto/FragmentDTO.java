package com.zhike.module_student.dto;

import lombok.Data;

@Data
public class FragmentDTO {
    private int id;
    private int videoId;
    private double startTime;
    private double endTime;
    private String contentText;
    private String keyframeUrl;
    private String asrText;
    private String visualTags;
    private String summary;
    private String[] keywords;
    private String vectorId;
}