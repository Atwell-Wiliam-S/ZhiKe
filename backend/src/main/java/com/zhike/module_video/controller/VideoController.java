package com.zhike.module_video.controller;

import com.zhike.common.result.Result;
import com.zhike.module_video.dto.VideoDTO;
import com.zhike.module_video.service.VideoAnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/video")
public class VideoController {

    @Autowired
    private VideoAnalysisService videoAnalysisService;

    // 上传视频
    @PostMapping("/upload")
    public Result<VideoDTO> uploadVideo(@RequestParam("file") MultipartFile file,
                                       @RequestParam("courseId") Integer courseId,
                                       @RequestParam("title") String title) {
        VideoDTO videoDTO = videoAnalysisService.uploadVideo(file, courseId, title);
        return Result.success(videoDTO);
    }

    // 触发视频解析
    @PostMapping("/{id}/parse")
    public Result<Void> triggerParse(@PathVariable("id") Integer videoId) {
        videoAnalysisService.triggerVideoParse(videoId);
        return Result.success();
    }

    // 查询解析状态
    @GetMapping("/{id}/status")
    public Result<Integer> getParseStatus(@PathVariable("id") Integer videoId) {
        Integer status = videoAnalysisService.getParseStatus(videoId);
        return Result.success(status);
    }

    // 获取签名URL
    @GetMapping("/{id}/sign-url")
    public Result<String> getSignUrl(@PathVariable("id") Integer videoId) {
        String signUrl = videoAnalysisService.getSignUrl(videoId);
        return Result.success(signUrl);
    }
}
