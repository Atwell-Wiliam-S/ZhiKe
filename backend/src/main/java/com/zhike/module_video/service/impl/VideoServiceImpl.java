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

@Service
public class VideoServiceImpl implements VideoService {

    @Autowired
    private VideoMapper videoMapper;

    @Autowired
    private KnowledgeFragmentMapper knowledgeFragmentMapper;

    @Override
    public Map<String, String> getUploadSignature(String fileName, long fileSize, Long courseId) {
        // 这里应该实现OSS签名URL生成逻辑
        // 为了演示，这里返回模拟数据
        Map<String, String> signature = new HashMap<>();
        signature.put("uploadUrl", "https://oss.zhike.edu.cn/upload" + fileName);
        signature.put("signUrl", "https://oss.zhike.edu.cn/videos" + fileName);
        signature.put("uploadId", "test-upload-id");
        return signature;
    }

    @Override
    public Video uploadComplete(Long courseId, String title, String ossUrl, Integer duration) {
        Video video = new Video();
        video.setCourseId(courseId);
        video.setTitle(title);
        video.setOssUrl(ossUrl);
        video.setDuration(duration);
        video.setParseStatus(0); // 0: 未解析
        video.setOrderNum(0);
        video.setCreatedAt(LocalDateTime.now());
        video.setUpdatedAt(LocalDateTime.now());
        videoMapper.insert(video);
        return video;
    }

    @Override
    public void triggerParse(Long videoId) {
        Video video = videoMapper.findById(videoId);
        if (video != null && video.getParseStatus() != 1) { // 1: 解析中
            videoMapper.updateParseStatus(videoId, 1);
            // 这里应该触发异步解析任务
        }
    }

    @Override
    public Map<String, Object> getParseStatus(Long videoId) {
        Video video = videoMapper.findById(videoId);
        Map<String, Object> status = new HashMap<>();
        if (video != null) {
            status.put("videoId", videoId);
            status.put("parseStatus", video.getParseStatus());
            status.put("parseStatusDesc", getParseStatusDesc(video.getParseStatus()));
            // 这里应该查询知识片段数量
            List<KnowledgeFragment> fragments = knowledgeFragmentMapper.findByVideoId(videoId);
            status.put("fragmentCount", fragments.size());
            status.put("duration", video.getDuration());
        }
        return status;
    }

    @Override
    public Map<String, Object> getSignUrl(Long videoId) {
        Video video = videoMapper.findById(videoId);
        Map<String, Object> signUrl = new HashMap<>();
        if (video != null) {
            signUrl.put("videoId", videoId);
            signUrl.put("signUrl", video.getOssUrl()); // 这里应该生成带签名的URL
            signUrl.put("expiresAt", LocalDateTime.now().plusHours(1).toString());
        }
        return signUrl;
    }

    @Override
    public List<Video> getTeacherVideos(Long courseId, Integer page, Integer pageSize) {
        // 这里应该实现分页查询
        return videoMapper.findByCourseId(courseId);
    }

    @Override
    public List<KnowledgeFragment> getFragments(Long videoId) {
        return knowledgeFragmentMapper.findByVideoId(videoId);
    }

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

    @Override
    public void deleteFragment(Long fragmentId) {
        knowledgeFragmentMapper.deleteById(fragmentId);
    }

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
