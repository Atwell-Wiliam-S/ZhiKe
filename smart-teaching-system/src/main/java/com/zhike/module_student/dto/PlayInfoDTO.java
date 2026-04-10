package com.zhike.module_student.dto;

import lombok.Data;
import java.util.List;

@Data
public class PlayInfoDTO {
    private int videoId;
    private String title;
    private String signUrl;
    private int duration;
    private List<FragmentDTO> fragments;
    private Double lastWatchTime;
}