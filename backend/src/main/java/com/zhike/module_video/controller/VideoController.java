package com.zhike.module_video.controller;

import com.zhike.common.util.Result;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/video")
public class VideoController {

    @GetMapping("/list")
    public Result<?> getVideoList() {
        return Result.success("视频列表");
    }

    @GetMapping("/{id}")
    public Result<?> getVideoById(@PathVariable Long id) {
        return Result.success("视频详情");
    }

    @PostMapping("/upload")
    public Result<?> uploadVideo() {
        return Result.success("视频上传成功");
    }

    @PutMapping("/{id}")
    public Result<?> updateVideo(@PathVariable Long id) {
        return Result.success("视频更新成功");
    }

    @DeleteMapping("/{id}")
    public Result<?> deleteVideo(@PathVariable Long id) {
        return Result.success("视频删除成功");
    }

}