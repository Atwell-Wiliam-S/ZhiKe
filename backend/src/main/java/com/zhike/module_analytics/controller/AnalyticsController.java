package com.zhike.module_analytics.controller;

import com.zhike.common.util.Result;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/analytics")
public class AnalyticsController {

    @GetMapping("/heatmap/{videoId}")
    public Result<?> getHeatmap(@PathVariable Long videoId) {
        return Result.success("热力图数据");
    }

    @GetMapping("/classOverview")
    public Result<?> getClassOverview() {
        return Result.success("班级概览");
    }

    @GetMapping("/studentProfile/{userId}")
    public Result<?> getStudentProfile(@PathVariable Long userId) {
        return Result.success("学生画像");
    }

    @GetMapping("/wordCloud/{videoId}")
    public Result<?> getWordCloud(@PathVariable Long videoId) {
        return Result.success("词云数据");
    }

}