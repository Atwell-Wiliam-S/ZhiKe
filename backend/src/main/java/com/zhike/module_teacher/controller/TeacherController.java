package com.zhike.module_teacher.controller;

import com.zhike.common.result.Result;
import com.zhike.module_video.dto.FragmentDTO;
import com.zhike.module_video.dto.VideoDTO;
import com.zhike.module_video.service.VideoAnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/teacher")
public class TeacherController {

    @Autowired
    private VideoAnalysisService videoAnalysisService;

    // 获取教师视频列表
    @GetMapping("/videos")
    public Result<List<VideoDTO>> getVideoList(@RequestParam("courseId") Integer courseId,
                                              @RequestParam(defaultValue = "1") Integer page,
                                              @RequestParam(defaultValue = "10") Integer pageSize) {
        List<VideoDTO> videoDTOs = videoAnalysisService.getVideoList(courseId, page, pageSize);
        return Result.success(videoDTOs);
    }

    // 获取知识片段列表
    @GetMapping("/fragments")
    public Result<List<FragmentDTO>> getFragmentList(@RequestParam("videoId") Integer videoId) {
        List<FragmentDTO> fragmentDTOs = videoAnalysisService.getFragmentList(videoId);
        return Result.success(fragmentDTOs);
    }

    // 更新知识片段
    @PutMapping("/fragments/{id}")
    public Result<Void> updateFragment(@PathVariable("id") Integer fragmentId,
                                       @RequestParam("contentText") String contentText,
                                       @RequestParam("summary") String summary,
                                       @RequestParam("keywords") String[] keywords) {
        videoAnalysisService.updateFragment(fragmentId, contentText, summary, keywords);
        return Result.success();
    }
}
