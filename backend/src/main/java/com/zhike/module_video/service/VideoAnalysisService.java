package com.zhike.module_video.service;

import com.zhike.module_video.dto.FragmentDTO;
import com.zhike.module_video.dto.VideoDTO;
import com.zhike.module_video.entity.Video;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface VideoAnalysisService {
    // 上传视频
    VideoDTO uploadVideo(MultipartFile file, Integer courseId, String title);

    // 触发视频解析
    void triggerVideoParse(Integer videoId);

    // 查询解析状态
    Integer getParseStatus(Integer videoId);

    // 获取视频签名URL
    String getSignUrl(Integer videoId);

    // 获取视频列表
    List<VideoDTO> getVideoList(Integer courseId, Integer page, Integer pageSize);

    // 获取知识片段列表
    List<FragmentDTO> getFragmentList(Integer videoId);

    // 更新知识片段
    void updateFragment(Integer fragmentId, String contentText, String summary, String[] keywords);
}
