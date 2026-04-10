package com.zhike.module_analytics.controller;

import com.zhike.common.util.Result;
import com.zhike.module_analytics.service.AnalyticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/analytics")
public class AnalyticsController {

    @Autowired
    private AnalyticsService analyticsService;

    @GetMapping("/heatmap")
    public Result<?> getHeatmap(@RequestParam Long courseId, @RequestParam Long videoId) {
        return Result.success(analyticsService.getHeatmap(courseId, videoId));
    }

    @GetMapping("/class-overview")
    public Result<?> getClassOverview(@RequestParam Long courseId) {
        return Result.success(analyticsService.getClassOverview(courseId));
    }

    @GetMapping("/student-profile")
    public Result<?> getStudentProfile(@RequestParam Long studentId) {
        return Result.success(analyticsService.getStudentProfile(studentId));
    }

    @GetMapping("/wordcloud")
    public Result<?> getWordcloud(@RequestParam Long courseId, @RequestParam(required = false) String startDate, @RequestParam(required = false) String endDate) {
        return Result.success(analyticsService.getWordcloud(courseId, startDate, endDate));
    }
}
