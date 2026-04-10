package com.zhike.module_analytics.service;

import java.util.Map;

public interface AnalyticsService {

    Map<String, Object> getHeatmap(Long courseId, Long videoId);

    Map<String, Object> getClassOverview(Long courseId);

    Map<String, Object> getStudentProfile(Long studentId);

    Map<String, Object> getWordcloud(Long courseId, String startDate, String endDate);
}
