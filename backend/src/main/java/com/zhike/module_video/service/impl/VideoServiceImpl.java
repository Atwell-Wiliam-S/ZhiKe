package com.zhike.module_video.service.impl;

import com.zhike.module_video.mapper.VideoMapper;
import com.zhike.module_video.mapper.KnowledgeFragmentMapper;
import com.zhike.module_video.model.Video;
import com.zhike.module_video.model.KnowledgeFragment;
import com.zhike.module_video.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 视频服务实现类
 * 实现视频上传、解析、管理等业务逻辑
 */
@Service
public class VideoServiceImpl implements VideoService {

    @Autowired
    private VideoMapper videoMapper;

    @Autowired
    private KnowledgeFragmentMapper knowledgeFragmentMapper;

    /**
     * 获取视频上传签名
     * 用于前端直接上传视频到OSS
     * 注意：当前为模拟实现，实际应集成OSS服务生成签名URL
     * 
     * @param fileName 文件名
     * @param fileSize 文件大小（字节）
     * @param courseId 课程ID
     * @return 包含上传URL、签名URL和上传ID的Map
     */
    @Override
    public Map<String, String> getUploadSignature(String fileName, long fileSize, Long courseId) {
        Map<String, String> signature = new HashMap<>();
        signature.put("uploadUrl", "https://oss.zhike.edu.cn/upload" + fileName);
        signature.put("signUrl", "https://oss.zhike.edu.cn/videos" + fileName);
        signature.put("uploadId", "test-upload-id");
        return signature;
    }

    /**
     * 视频上传完成后的处理
     * 创建视频记录并初始化解析状态
     * 
     * @param courseId 课程ID
     * @param title 视频标题
     * @param ossUrl 视频在OSS上的URL
     * @param duration 视频时长（秒）
     * @return 创建的视频对象
     */
    @Override
    public Video uploadComplete(Long courseId, String title, String ossUrl, Integer duration) {
        Video video = new Video();
        video.setCourseId(courseId);
        video.setTitle(title);
        video.setOssUrl(ossUrl);
        video.setDuration(duration);
        video.setParseStatus(0);
        video.setOrderNum(0);
        video.setCreatedAt(LocalDateTime.now());
        video.setUpdatedAt(LocalDateTime.now());
        videoMapper.insert(video);
        return video;
    }

    /**
     * 触发视频解析
     * 将视频解析状态更新为解析中
     * 注意：当前为模拟实现，实际应触发异步解析任务
     * 
     * @param videoId 视频ID
     */
    @Override
    public void triggerParse(Long videoId) {
        Video video = videoMapper.findById(videoId);
        if (video != null && video.getParseStatus() != 1) {
            videoMapper.updateParseStatus(videoId, 1);
        }
    }

    /**
     * 获取视频解析状态
     * 
     * @param videoId 视频ID
     * @return 包含解析状态、知识片段数量、时长等信息的Map
     */
    @Override
    public Map<String, Object> getParseStatus(Long videoId) {
        Video video = videoMapper.findById(videoId);
        Map<String, Object> status = new HashMap<>();
        if (video != null) {
            status.put("videoId", videoId);
            status.put("parseStatus", video.getParseStatus());
            status.put("parseStatusDesc", getParseStatusDesc(video.getParseStatus()));
            List<KnowledgeFragment> fragments = knowledgeFragmentMapper.findByVideoId(videoId);
            status.put("fragmentCount", fragments.size());
            status.put("duration", video.getDuration());
        }
        return status;
    }

    /**
     * 获取视频播放签名URL
     * 用于前端播放受保护的视频资源
     * 注意：当前为模拟实现，实际应生成带签名的临时访问URL
     * 
     * @param videoId 视频ID
     * @return 包含签名URL和过期时间的Map
     */
    @Override
    public Map<String, Object> getSignUrl(Long videoId) {
        Video video = videoMapper.findById(videoId);
        Map<String, Object> signUrl = new HashMap<>();
        if (video != null) {
            signUrl.put("videoId", videoId);
            signUrl.put("signUrl", video.getOssUrl());
            signUrl.put("expiresAt", LocalDateTime.now().plusHours(1).toString());
        }
        return signUrl;
    }

    /**
     * 获取教师端的视频列表
     * 注意：当前为模拟实现，未实现分页功能
     * 
     * @param courseId 课程ID
     * @param page 页码
     * @param pageSize 每页数量
     * @return 视频列表
     */
    @Override
    public List<Video> getTeacherVideos(Long courseId, Integer page, Integer pageSize) {
        return videoMapper.findByCourseId(courseId);
    }

    /**
     * 获取视频的知识片段列表
     * 
     * @param videoId 视频ID
     * @return 知识片段列表
     */
    @Override
    public List<KnowledgeFragment> getFragments(Long videoId) {
        return knowledgeFragmentMapper.findByVideoId(videoId);
    }

    /**
     * 更新知识片段信息
     * 
     * @param fragmentId 知识片段ID
     * @param contentText 片段内容文本
     * @param summary 片段摘要
     * @param keywords 关键词
     */
    @Override
    public void updateFragment(Long fragmentId, String contentText, String summary, String keywords) {
        KnowledgeFragment fragment = knowledgeFragmentMapper.findById(fragmentId);
        if (fragment != null) {
            if (contentText != null) fragment.setContentText(contentText);
            if (summary != null) fragment.setSummary(summary);
            if (keywords != null) fragment.setKeywords(keywords);
            fragment.setUpdatedAt(LocalDateTime.now());
            knowledgeFragmentMapper.updateById(fragment);
        }
    }

    /**
     * 删除知识片段
     * 
     * @param fragmentId 知识片段ID
     */
    @Override
    public void deleteFragment(Long fragmentId) {
        knowledgeFragmentMapper.deleteById(fragmentId);
    }

    /**
     * 获取解析状态的描述文本
     * 
     * @param parseStatus 解析状态码
     * @return 状态描述文本
     */
    private String getParseStatusDesc(Integer parseStatus) {
        switch (parseStatus) {
            case 0: return "未解析";
            case 1: return "解析中";
            case 2: return "解析完成";
            case 3: return "解析失败";
            default: return "未知状态";
        }
    }
}
