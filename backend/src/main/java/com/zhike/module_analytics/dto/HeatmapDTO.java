package com.zhike.module_analytics.dto;

import lombok.Data;

import java.util.List;

@Data
public class HeatmapDTO {
    private Integer videoId;
    private Integer duration;
    private Integer windowSize;
    private List<HeatPoint> data;
}
