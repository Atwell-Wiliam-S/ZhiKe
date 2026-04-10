package com.zhike.module_student.controller;

import com.zhike.common.Result;
import com.zhike.module_student.dto.*;
import com.zhike.module_student.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/student")
public class StudentController {

    @Autowired
    private StudentService studentService;

    // 获取课程列表
    @GetMapping("/courses")
    public Result<List<CourseDTO>> getCourses(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        List<CourseDTO> courses = studentService.getCourses(keyword, page, pageSize);
        return Result.success(courses);
    }

    // 获取课程详情
    @GetMapping("/courses/{id}")
    public Result<CourseDetailDTO> getCourseDetail(@PathVariable("id") int courseId) {
        CourseDetailDTO detail = studentService.getCourseDetail(courseId);
        if (detail == null) {
            return Result.error(404, "课程不存在");
        }
        return Result.success(detail);
    }

    // 获取视频播放信息
    @GetMapping("/videos/{id}/play-info")
    public Result<PlayInfoDTO> getVideoPlayInfo(@PathVariable("id") int videoId) {
        // 模拟用户ID，实际应该从JWT中获取
        int userId = 1;
        PlayInfoDTO playInfo = studentService.getVideoPlayInfo(videoId, userId);
        if (playInfo == null) {
            return Result.error(404, "视频不存在");
        }
        return Result.success(playInfo);
    }

    // 心跳上报
    @PostMapping("/records/heartbeat")
    public Result<Void> heartbeat(@RequestBody HeartbeatRequest request) {
        // 模拟用户ID，实际应该从JWT中获取
        int userId = 1;
        studentService.heartbeat(request, userId);
        return Result.success();
    }

    // 保存进度
    @PostMapping("/records/progress")
    public Result<Void> saveProgress(@RequestBody ProgressRequest request) {
        // 模拟用户ID，实际应该从JWT中获取
        int userId = 1;
        studentService.saveProgress(request, userId);
        return Result.success();
    }

    // 添加笔记
    @PostMapping("/notes")
    public Result<Integer> addNote(@RequestBody NoteRequest request) {
        // 模拟用户ID，实际应该从JWT中获取
        int userId = 1;
        int noteId = studentService.addNote(request, userId);
        return Result.success(noteId);
    }

    // 获取笔记列表
    @GetMapping("/notes")
    public Result<List<NoteDTO>> getNotes(@RequestParam("videoId") int videoId) {
        // 模拟用户ID，实际应该从JWT中获取
        int userId = 1;
        List<NoteDTO> notes = studentService.getNotes(videoId, userId);
        return Result.success(notes);
    }
}