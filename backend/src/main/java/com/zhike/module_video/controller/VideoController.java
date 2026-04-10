package com.zhike.module_video.controller;

import com.zhike.common.util.Result;
import com.zhike.module_video.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 视频资源控制器
 * 处理视频上传、解析、状态查询等视频相关操作
 */
@RestController
@RequestMapping("/video")
public class VideoController {

    @Autowired
    private VideoService videoService;

    /**
     * 获取OSS上传签名
     * @param request 上传请求参数
     * @return 签名信息
     */
    @PostMapping("/upload-signature")
    public Result<?> getUploadSignature(@RequestBody Map<String, Object> request) {
        String fileName = (String) request.get("fileName");
        long fileSize = (Long) request.get("fileSize");
        Long courseId = (Long) request.get("courseId");

        Map<String, String> signature = videoService.getUploadSignature(fileName, fileSize, courseId);
        return Result.success(signature);
    }

    /**
     * 视频上传完成通知
     * @param request 上传完成请求参数
     * @return 视频信息
     */
    @PostMapping("/upload-complete")
    public Result<?> uploadComplete(@RequestBody Map<String, Object> request) {
        Long courseId = (Long) request.get("courseId");
        String title = (String) request.get("title");
        String ossUrl = (String) request.get("ossUrl");
        Integer duration = (Integer) request.get("duration");

        return Result.success(videoService.uploadComplete(courseId, title, ossUrl, duration));
    }

    /**
     * 触发视频解析
     * @param id 视频ID
     * @return 操作结果
     */
    @PostMapping("/{id}/parse")
    public Result<?> triggerParse(@PathVariable Long id) {
        videoService.triggerParse(id);
        return Result.success();
    }

    /**
     * 获取视频解析状态
     * @param id 视频ID
     * @return 解析状态
     */
    @GetMapping("/{id}/status")
    public Result<?> getParseStatus(@PathVariable Long id) {
        return Result.success(videoService.getParseStatus(id));
    }

    /**
     * 获取视频播放签名URL
     * @param id 视频ID
     * @return 签名URL信息
     */
    @GetMapping("/{id}/sign-url")
    public Result<?> getSignUrl(@PathVariable Long id) {
        return Result.success(videoService.getSignUrl(id));
    }
}
