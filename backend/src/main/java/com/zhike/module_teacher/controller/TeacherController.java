package com.zhike.module_teacher.controller;

import com.zhike.common.util.Result;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/teacher")
public class TeacherController {

    @GetMapping("/courses")
    public Result<?> getCourses() {
        return Result.success("课程列表");
    }

    @PostMapping("/courses")
    public Result<?> createCourse() {
        return Result.success("课程创建成功");
    }

    @PutMapping("/courses/{id}")
    public Result<?> updateCourse(@PathVariable Long id) {
        return Result.success("课程更新成功");
    }

    @DeleteMapping("/courses/{id}")
    public Result<?> deleteCourse(@PathVariable Long id) {
        return Result.success("课程删除成功");
    }

    @GetMapping("/analytics")
    public Result<?> getAnalytics() {
        return Result.success("学情分析");
    }

}