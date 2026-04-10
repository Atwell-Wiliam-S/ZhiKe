package com.zhike.module_teacher.controller;

import com.zhike.common.util.Result;
import com.zhike.module_video.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 教师功能控制器
 * 处理教师视频管理、知识片段管理等教师相关操作
 */
@RestController
@RequestMapping("/teacher")
public class TeacherController {

    @Autowired
    private VideoService videoService;

    /**
     * 获取教师的视频列表
     * @param courseId 课程ID
     * @param page 页码
     * @param pageSize 每页条数
     * @return 视频列表
     */
    @GetMapping("/videos")
    public Result<?> getVideos(@RequestParam Long courseId, @RequestParam Integer page, @RequestParam Integer pageSize) {
        return Result.success(videoService.getTeacherVideos(courseId, page, pageSize));
    }

    /**
     * 获取知识片段列表
     * @param videoId 视频ID
     * @return 知识片段列表
     */
    @GetMapping("/fragments")
    public Result<?> getFragments(@RequestParam Long videoId) {
        return Result.success(videoService.getFragments(videoId));
    }

    /**
     * 更新知识片段
     * @param id 片段ID
     * @param request 更新请求参数
     * @return 操作结果
     */
    @PutMapping("/fragments/{id}")
    public Result<?> updateFragment(@PathVariable Long id, @RequestBody Map<String, Object> request) {
        String contentText = (String) request.get("contentText");
        String summary = (String) request.get("summary");
        String keywords = (String) request.get("keywords");

        videoService.updateFragment(id, contentText, summary, keywords);
        return Result.success();
    }

    /**
     * 删除知识片段
     * @param id 片段ID
     * @return 操作结果
     */
    @DeleteMapping("/fragments/{id}")
    public Result<?> deleteFragment(@PathVariable Long id) {
        videoService.deleteFragment(id);
        return Result.success();
    }
}
