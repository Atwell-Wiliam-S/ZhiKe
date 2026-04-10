package com.zhike.module_video.service.impl;

import com.zhike.common.exception.BusinessException;
import com.zhike.module_ai_adapter.service.AiAdapterService;
import com.zhike.module_video.dto.FragmentDTO;
import com.zhike.module_video.dto.VideoDTO;
import com.zhike.module_video.entity.KnowledgeFragment;
import com.zhike.module_video.entity.Video;
import com.zhike.module_video.service.VideoAnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class VideoAnalysisServiceImpl implements VideoAnalysisService {

    @Autowired
    private AiAdapterService aiAdapterService;

    @Value("${video.upload-path}")
    private String uploadPath;

    @Value("${video.ffmpeg-path}")
    private String ffmpegPath;

    // 模拟数据库存储
    private final Map<Integer, Video> videoMap = new HashMap<>();
    private final Map<Integer, List<KnowledgeFragment>> fragmentMap = new HashMap<>();
    private int videoIdCounter = 1;
    private int fragmentIdCounter = 1;

    @Override
    public VideoDTO uploadVideo(MultipartFile file, Integer courseId, String title) {
        try {
            // 创建上传目录
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }

            // 生成文件名
            String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
            File dest = new File(uploadDir, fileName);

            // 保存文件
            file.transferTo(dest);

            // 创建视频记录
            Video video = new Video();
            video.setId(videoIdCounter++);
            video.setCourseId(courseId);
            video.setTitle(title);
            video.setOssUrl("http://oss.example.com/videos/" + fileName);
            video.setDuration(600); // 模拟10分钟视频
            video.setParseStatus(0); // 待解析
            video.setThumbnailUrl("http://oss.example.com/thumbnails/" + fileName + ".jpg");
            video.setOrderNum(0);

            videoMap.put(video.getId(), video);

            // 构建DTO
            VideoDTO videoDTO = new VideoDTO();
            videoDTO.setId(video.getId());
            videoDTO.setCourseId(video.getCourseId());
            videoDTO.setTitle(video.getTitle());
            videoDTO.setDuration(video.getDuration());
            videoDTO.setParseStatus(video.getParseStatus());
            videoDTO.setThumbnailUrl(video.getThumbnailUrl());
            videoDTO.setOrderNum(video.getOrderNum());
            videoDTO.setCreatedAt("2026-04-09 12:00:00");

            return videoDTO;
        } catch (IOException e) {
            throw BusinessException.error("视频上传失败: " + e.getMessage());
        }
    }

    @Override
    public void triggerVideoParse(Integer videoId) {
        Video video = videoMap.get(videoId);
        if (video == null) {
            throw BusinessException.notFound("视频不存在");
        }

        if (video.getParseStatus() == 1) {
            throw BusinessException.conflict("视频正在解析中");
        }

        // 更新解析状态为进行中
        video.setParseStatus(1);

        // 模拟视频解析过程
        new Thread(() -> {
            try {
                // 模拟解析延迟
                Thread.sleep(5000);

                // 生成知识片段
                List<KnowledgeFragment> fragments = generateFragments(videoId);
                fragmentMap.put(videoId, fragments);

                // 更新解析状态为完成
                video.setParseStatus(2);
            } catch (Exception e) {
                // 更新解析状态为失败
                video.setParseStatus(3);
            }
        }).start();
    }

    @Override
    public Integer getParseStatus(Integer videoId) {
        Video video = videoMap.get(videoId);
        if (video == null) {
            throw BusinessException.notFound("视频不存在");
        }
        return video.getParseStatus();
    }

    @Override
    public String getSignUrl(Integer videoId) {
        Video video = videoMap.get(videoId);
        if (video == null) {
            throw BusinessException.notFound("视频不存在");
        }
        // 模拟生成签名URL
        return video.getOssUrl() + "?sign=xxx&expire=1234567890";
    }

    @Override
    public List<VideoDTO> getVideoList(Integer courseId, Integer page, Integer pageSize) {
        List<VideoDTO> videoDTOs = new ArrayList<>();
        for (Video video : videoMap.values()) {
            if (video.getCourseId().equals(courseId)) {
                VideoDTO videoDTO = new VideoDTO();
                videoDTO.setId(video.getId());
                videoDTO.setCourseId(video.getCourseId());
                videoDTO.setTitle(video.getTitle());
                videoDTO.setDuration(video.getDuration());
                videoDTO.setParseStatus(video.getParseStatus());
                videoDTO.setThumbnailUrl(video.getThumbnailUrl());
                videoDTO.setOrderNum(video.getOrderNum());
                videoDTO.setCreatedAt("2026-04-09 12:00:00");
                videoDTOs.add(videoDTO);
            }
        }
        // 模拟分页
        int start = (page - 1) * pageSize;
        int end = Math.min(start + pageSize, videoDTOs.size());
        if (start >= videoDTOs.size()) {
            return new ArrayList<>();
        }
        return videoDTOs.subList(start, end);
    }

    @Override
    public List<FragmentDTO> getFragmentList(Integer videoId) {
        List<KnowledgeFragment> fragments = fragmentMap.get(videoId);
        if (fragments == null) {
            return new ArrayList<>();
        }

        List<FragmentDTO> fragmentDTOs = new ArrayList<>();
        for (KnowledgeFragment fragment : fragments) {
            FragmentDTO fragmentDTO = new FragmentDTO();
            fragmentDTO.setId(fragment.getId());
            fragmentDTO.setVideoId(fragment.getVideoId());
            fragmentDTO.setStartTime(fragment.getStartTime());
            fragmentDTO.setEndTime(fragment.getEndTime());
            fragmentDTO.setContentText(fragment.getContentText());
            fragmentDTO.setSummary(fragment.getSummary());
            fragmentDTO.setKeywords(new String[]{"知识点", "学习", "教育"});
            fragmentDTO.setVectorId(fragment.getVectorId());
            fragmentDTOs.add(fragmentDTO);
        }

        return fragmentDTOs;
    }

    @Override
    public void updateFragment(Integer fragmentId, String contentText, String summary, String[] keywords) {
        // 模拟更新知识片段
        for (List<KnowledgeFragment> fragments : fragmentMap.values()) {
            for (KnowledgeFragment fragment : fragments) {
                if (fragment.getId().equals(fragmentId)) {
                    fragment.setContentText(contentText);
                    fragment.setSummary(summary);
                    // 这里简化处理，实际应该将keywords转换为JSON存储
                    return;
                }
            }
        }
        throw BusinessException.notFound("知识片段不存在");
    }

    // 模拟生成知识片段
    private List<KnowledgeFragment> generateFragments(Integer videoId) {
        List<KnowledgeFragment> fragments = new ArrayList<>();

        // 生成3个模拟知识片段
        for (int i = 0; i < 3; i++) {
            KnowledgeFragment fragment = new KnowledgeFragment();
            fragment.setId(fragmentIdCounter++);
            fragment.setVideoId(videoId);
            fragment.setStartTime(i * 120.0); // 每2分钟一个片段
            fragment.setEndTime((i + 1) * 120.0);
            fragment.setContentText("这是第" + (i + 1) + "个知识片段的内容，包含了重要的知识点。");
            fragment.setKeyframeUrl("http://oss.example.com/keyframes/" + videoId + "_" + i + ".jpg");
            fragment.setAsrText("这是语音转写的文本内容。");
            fragment.setVisualTags("{\"tags\":[\"教育\",\"学习\",\"知识点\"]}");
            fragment.setSummary("第" + (i + 1) + "个知识片段的摘要");
            fragment.setKeywords("{\"keywords\":[\"知识点\",\"学习\",\"教育\"]}");
            fragment.setVectorId(UUID.randomUUID().toString());
            fragments.add(fragment);
        }

        return fragments;
    }
}
