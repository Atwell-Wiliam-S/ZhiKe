package com.zhike.module_student.controller;

import com.zhike.common.util.Result;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/student")
public class StudentController {

    @GetMapping("/courses")
    public Result<?> getCourses() {
        return Result.success("课程列表");
    }

    @GetMapping("/progress")
    public Result<?> getLearningProgress() {
        return Result.success("学习进度");
    }

    @PostMapping("/notes")
    public Result<?> createNote() {
        return Result.success("笔记创建成功");
    }

    @GetMapping("/notes")
    public Result<?> getNotes() {
        return Result.success("笔记列表");
    }

    @GetMapping("/analytics")
    public Result<?> getPersonalAnalytics() {
        return Result.success("个人学情分析");
    }

}