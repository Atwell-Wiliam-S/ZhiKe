package com.zhike.module_analytics.service;

import java.util.Map;

/**
 * 学情分析服务接口
 * 提供学习行为数据的统计分析功能，包括热力图、班级概览、学生画像、词云等
 */
public interface AnalyticsService {

    /**
     * 获取视频观看热力图数据
     * 分析学生在视频不同时间段的观看行为，识别难点和重点
     * 
     * @param courseId 课程ID
     * @param videoId 视频ID
     * @return 包含热力图数据的Map
     */
    Map<String, Object> getHeatmap(Long courseId, Long videoId);

    /**
     * 获取班级学习概览
     * 统计班级整体的学习情况数据
     * 
     * @param courseId 课程ID
     * @return 包含班级概览数据的Map
     */
    Map<String, Object> getClassOverview(Long courseId);

    /**
     * 获取学生个人画像
     * 分析学生的学习行为、进度、薄弱点等
     * 
     * @param studentId 学生ID
     * @return 包含学生画像数据的Map
     */
    Map<String, Object> getStudentProfile(Long studentId);

    /**
     * 获取学习关键词词云
     * 分析指定时间段内学生提问和学习内容中的关键词
     * 
     * @param courseId 课程ID
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 包含词云数据的Map
     */
    Map<String, Object> getWordcloud(Long courseId, String startDate, String endDate);
}
