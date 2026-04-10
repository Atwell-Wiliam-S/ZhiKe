package com.zhike.module_student.controller;

import com.zhike.common.result.Result;
import com.zhike.module_student.dto.*;
import com.zhike.module_student.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/student")
public class StudentController {

    @Autowired
    private StudentService studentService;

    // 获取课程列表
    @GetMapping("/courses")
    public Result<List<CourseDTO>> getCourseList(@RequestParam(required = false) String keyword,
                                               @RequestParam(defaultValue = "1") Integer page,
                                               @RequestParam(defaultValue = "10") Integer pageSize) {
        List<CourseDTO> courses = studentService.getCourseList(keyword, page, pageSize);
        return Result.success(courses);
    }

    // 获取课程详情
    @GetMapping("/courses/{id}")
    public Result<CourseDetailDTO> getCourseDetail(@PathVariable("id") Integer courseId) {
        CourseDetailDTO courseDetail = studentService.getCourseDetail(courseId);
        return Result.success(courseDetail);
    }

    // 获取视频播放信息
    @GetMapping("/videos/{id}/play-info")
    public Result<PlayInfoDTO> getPlayInfo(@PathVariable("id") Integer videoId) {
        PlayInfoDTO playInfo = studentService.getPlayInfo(videoId);
        return Result.success(playInfo);
    }

    // 心跳上报
    @PostMapping("/records/heartbeat")
    public Result<Void> reportHeartbeat(@RequestBody HeartbeatRequest request) {
        studentService.reportHeartbeat(request);
        return Result.success();
    }

    // 保存学习进度
    @PostMapping("/records/progress")
    public Result<Void> saveProgress(@RequestBody ProgressRequest request) {
        studentService.saveProgress(request);
        return Result.success();
    }

    // 添加笔记
    @PostMapping("/notes")
    public Result<Integer> addNote(@RequestBody NoteRequest request) {
        Integer noteId = studentService.addNote(request);
        return Result.success(noteId);
    }

    // 获取笔记列表
    @GetMapping("/notes")
    public Result<List<NoteDTO>> getNoteList(@RequestParam("videoId") Integer videoId) {
        List<NoteDTO> notes = studentService.getNoteList(videoId);
        return Result.success(notes);
    }
}
