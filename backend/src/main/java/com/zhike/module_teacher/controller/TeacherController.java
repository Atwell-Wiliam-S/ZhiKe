package com.zhike.module_teacher.controller;

import com.zhike.common.util.Result;
import com.zhike.module_video.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/teacher")
public class TeacherController {

    @Autowired
    private VideoService videoService;

    @GetMapping("/videos")
    public Result<?> getVideos(@RequestParam Long courseId, @RequestParam Integer page, @RequestParam Integer pageSize) {
        return Result.success(videoService.getTeacherVideos(courseId, page, pageSize));
    }

    @GetMapping("/fragments")
    public Result<?> getFragments(@RequestParam Long videoId) {
        return Result.success(videoService.getFragments(videoId));
    }

    @PutMapping("/fragments/{id}")
    public Result<?> updateFragment(@PathVariable Long id, @RequestBody Map<String, Object> request) {
        String contentText = (String) request.get("contentText");
        String summary = (String) request.get("summary");
        String keywords = (String) request.get("keywords");

        videoService.updateFragment(id, contentText, summary, keywords);
        return Result.success();
    }

    @DeleteMapping("/fragments/{id}")
    public Result<?> deleteFragment(@PathVariable Long id) {
        videoService.deleteFragment(id);
        return Result.success();
    }
}
