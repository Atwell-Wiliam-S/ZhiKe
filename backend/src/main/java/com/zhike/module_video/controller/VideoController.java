package com.zhike.module_video.controller;

import com.zhike.common.util.Result;
import com.zhike.module_video.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/video")
public class VideoController {

    @Autowired
    private VideoService videoService;

    @PostMapping("/upload-signature")
    public Result<?> getUploadSignature(@RequestBody Map<String, Object> request) {
        String fileName = (String) request.get("fileName");
        long fileSize = (Long) request.get("fileSize");
        Long courseId = (Long) request.get("courseId");

        Map<String, String> signature = videoService.getUploadSignature(fileName, fileSize, courseId);
        return Result.success(signature);
    }

    @PostMapping("/upload-complete")
    public Result<?> uploadComplete(@RequestBody Map<String, Object> request) {
        Long courseId = (Long) request.get("courseId");
        String title = (String) request.get("title");
        String ossUrl = (String) request.get("ossUrl");
        Integer duration = (Integer) request.get("duration");

        return Result.success(videoService.uploadComplete(courseId, title, ossUrl, duration));
    }

    @PostMapping("/{id}/parse")
    public Result<?> triggerParse(@PathVariable Long id) {
        videoService.triggerParse(id);
        return Result.success();
    }

    @GetMapping("/{id}/status")
    public Result<?> getParseStatus(@PathVariable Long id) {
        return Result.success(videoService.getParseStatus(id));
    }

    @GetMapping("/{id}/sign-url")
    public Result<?> getSignUrl(@PathVariable Long id) {
        return Result.success(videoService.getSignUrl(id));
    }
}
