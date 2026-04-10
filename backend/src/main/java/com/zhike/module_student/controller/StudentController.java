package com.zhike.module_student.controller;

import com.zhike.common.util.Result;
import com.zhike.module_student.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 学生功能控制器
 * 处理学生课程、视频学习、笔记管理等学生相关操作
 */
@RestController
@RequestMapping("/student")
public class StudentController {

    @Autowired
    private StudentService studentService;

    /**
     * 获取课程列表
     * @param keyword 搜索关键词
     * @param page 页码
     * @param pageSize 每页条数
     * @return 课程列表
     */
    @GetMapping("/courses")
    public Result<?> getCourses(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        return Result.success(studentService.getCourses(keyword, page, pageSize));
    }

    /**
     * 获取课程详情
     * @param courseId 课程ID
     * @return 课程详情
     */
    @GetMapping("/courses/{id}")
    public Result<?> getCourseDetail(@PathVariable("id") Long courseId) {
        Map<String, Object> detail = studentService.getCourseDetail(courseId);
        if (detail == null) {
            return Result.success("课程不存在");
        }
        return Result.success(detail);
    }

    /**
     * 获取视频播放信息
     * @param videoId 视频ID
     * @return 播放信息
     */
    @GetMapping("/videos/{id}/play-info")
    public Result<?> getVideoPlayInfo(@PathVariable("id") Long videoId) {
        // 模拟用户ID，实际应该从JWT中获取
        Long userId = 1L;
        Map<String, Object> playInfo = studentService.getPlayInfo(videoId, userId);
        if (playInfo == null) {
            return Result.success("视频不存在");
        }
        return Result.success(playInfo);
    }

    /**
     * 心跳上报
     * @param videoId 视频ID
     * @param currentTime 当前播放时间
     * @param status 播放状态
     * @return 操作结果
     */
    @PostMapping("/records/heartbeat")
    public Result<?> heartbeat(
            @RequestParam("videoId") Long videoId,
            @RequestParam("currentTime") Double currentTime,
            @RequestParam("status") String status) {
        // 模拟用户ID，实际应该从JWT中获取
        Long userId = 1L;
        studentService.heartbeat(userId, videoId, currentTime, status);
        return Result.success("心跳上报成功");
    }

    /**
     * 保存进度
     * @param videoId 视频ID
     * @param currentTime 当前时间
     * @param duration 视频时长
     * @return 操作结果
     */
    @PostMapping("/records/progress")
    public Result<?> saveProgress(
            @RequestParam("videoId") Long videoId,
            @RequestParam("currentTime") Double currentTime,
            @RequestParam("duration") Integer duration) {
        // 模拟用户ID，实际应该从JWT中获取
        Long userId = 1L;
        studentService.saveProgress(userId, videoId, currentTime, duration);
        return Result.success("进度保存成功");
    }

    /**
     * 添加笔记
     * @param videoId 视频ID
     * @param timestamp 时间戳
     * @param content 笔记内容
     * @return 笔记ID
     */
    @PostMapping("/notes")
    public Result<?> addNote(
            @RequestParam("videoId") Long videoId,
            @RequestParam("timestamp") Double timestamp,
            @RequestParam("content") String content) {
        // 模拟用户ID，实际应该从JWT中获取
        Long userId = 1L;
        Long noteId = studentService.addNote(userId, videoId, timestamp, content);
        return Result.success(noteId);
    }

    /**
     * 获取笔记列表
     * @param videoId 视频ID
     * @return 笔记列表
     */
    @GetMapping("/notes")
    public Result<?> getNotes(@RequestParam("videoId") Long videoId) {
        // 模拟用户ID，实际应该从JWT中获取
        Long userId = 1L;
        Map<String, Object> notes = studentService.getNotes(userId, videoId);
        return Result.success(notes);
    }

    /**
     * 获取学习进度
     * @return 学习进度
     */
    @GetMapping("/progress")
    public Result<?> getLearningProgress() {
        return Result.success("学习进度");
    }

    /**
     * 获取个人学情分析
     * @return 个人学情分析
     */
    @GetMapping("/analytics")
    public Result<?> getPersonalAnalytics() {
        return Result.success("个人学情分析");
    }

}