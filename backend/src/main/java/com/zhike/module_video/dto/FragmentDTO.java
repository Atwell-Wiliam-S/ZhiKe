package com.zhike.module_video.dto;

import lombok.Data;

@Data
public class FragmentDTO {
    private Integer id;
    private Integer videoId;
    private Double startTime;
    private Double endTime;
    private String contentText;
    private String summary;
    private String[] keywords;
    private String vectorId;
}
