package com.zhike.module_analytics.service;

import com.zhike.module_analytics.dto.*;

public interface LearningAnalyticsService {
    // 生成难点热力图数据
    HeatmapDTO generateHeatmap(Integer courseId, Integer videoId);

    // 获取班级学情概览
    ClassOverviewDTO getClassOverview(Integer courseId);

    // 获取学生学习画像
    StudentProfileDTO getStudentProfile(Integer studentId);

    // 生成高频困惑词云数据
    WordCloudDTO generateWordCloud(Integer courseId, String startDate, String endDate);
}
