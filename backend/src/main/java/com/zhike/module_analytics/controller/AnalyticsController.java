package com.zhike.module_analytics.controller;

import com.zhike.common.util.Result;
import com.zhike.module_analytics.service.AnalyticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 学情分析控制器
 * 处理难点热力图、班级学情概览、学生学习画像、高频困惑词云等学情分析操作
 */
@RestController
@RequestMapping("/analytics")
public class AnalyticsController {

    @Autowired
    private AnalyticsService analyticsService;

    /**
     * 获取难点热力图
     * @param courseId 课程ID
     * @param videoId 视频ID
     * @return 热力图数据
     */
    @GetMapping("/heatmap")
    public Result<?> getHeatmap(@RequestParam Long courseId, @RequestParam Long videoId) {
        return Result.success(analyticsService.getHeatmap(courseId, videoId));
    }

    /**
     * 获取班级学情概览
     * @param courseId 课程ID
     * @return 班级学情概览数据
     */
    @GetMapping("/class-overview")
    public Result<?> getClassOverview(@RequestParam Long courseId) {
        return Result.success(analyticsService.getClassOverview(courseId));
    }

    /**
     * 获取学生学习画像
     * @param studentId 学生ID
     * @return 学生学习画像数据
     */
    @GetMapping("/student-profile")
    public Result<?> getStudentProfile(@RequestParam Long studentId) {
        return Result.success(analyticsService.getStudentProfile(studentId));
    }

    /**
     * 获取高频困惑词云
     * @param courseId 课程ID
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 词云数据
     */
    @GetMapping("/wordcloud")
    public Result<?> getWordcloud(@RequestParam Long courseId, @RequestParam(required = false) String startDate, @RequestParam(required = false) String endDate) {
        return Result.success(analyticsService.getWordcloud(courseId, startDate, endDate));
    }
}
