package com.zhike.module_student.service.impl;

import com.zhike.module_student.dto.*;
import com.zhike.module_student.service.StudentService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class StudentServiceImpl implements StudentService {

    // 模拟课程数据
    private List<CourseDTO> mockCourses = new ArrayList<>();
    
    // 模拟视频数据
    private List<VideoDTO> mockVideos = new ArrayList<>();
    
    // 模拟知识片段数据
    private List<FragmentDTO> mockFragments = new ArrayList<>();
    
    // 模拟笔记数据
    private List<NoteDTO> mockNotes = new ArrayList<>();
    
    // 模拟学习记录
    private List<LearningRecord> mockLearningRecords = new ArrayList<>();
    
    // 初始化模拟数据
    public StudentServiceImpl() {
        // 初始化课程数据
        CourseDTO course1 = new CourseDTO();
        course1.setId(1);
        course1.setTitle("高等数学 A");
        course1.setTeacherName("李教授");
        course1.setDescription("涵盖微积分、线性代数等内容");
        course1.setCoverImage("/covers/math-a.jpg");
        course1.setVideoCount(24);
        course1.setPublishedAt("2024-01-10T08:00:00+08:00");
        mockCourses.add(course1);
        
        CourseDTO course2 = new CourseDTO();
        course2.setId(2);
        course2.setTitle("大学物理");
        course2.setTeacherName("王教授");
        course2.setDescription("涵盖力学、热学、电磁学等内容");
        course2.setCoverImage("/covers/physics.jpg");
        course2.setVideoCount(18);
        course2.setPublishedAt("2024-01-15T09:00:00+08:00");
        mockCourses.add(course2);
        
        // 初始化视频数据
        VideoDTO video1 = new VideoDTO();
        video1.setId(1);
        video1.setCourseId(1);
        video1.setTitle("第一章：函数与极限");
        video1.setOssUrl("https://oss.zhike.edu.cn/videos/math-01.mp4");
        video1.setDuration(600);
        video1.setParseStatus(2);
        video1.setParseStatusDesc("解析完成");
        video1.setThumbnailUrl("/thumbnails/math-01.jpg");
        video1.setOrderNum(1);
        video1.setCreatedAt("2024-01-10T08:00:00+08:00");
        mockVideos.add(video1);
        
        VideoDTO video2 = new VideoDTO();
        video2.setId(2);
        video2.setCourseId(1);
        video2.setTitle("第二章：导数与微分");
        video2.setOssUrl("https://oss.zhike.edu.cn/videos/math-02.mp4");
        video2.setDuration(720);
        video2.setParseStatus(2);
        video2.setParseStatusDesc("解析完成");
        video2.setThumbnailUrl("/thumbnails/math-02.jpg");
        video2.setOrderNum(2);
        video2.setCreatedAt("2024-01-12T08:00:00+08:00");
        mockVideos.add(video2);
        
        // 初始化知识片段数据
        FragmentDTO fragment1 = new FragmentDTO();
        fragment1.setId(1);
        fragment1.setVideoId(1);
        fragment1.setStartTime(0);
        fragment1.setEndTime(120);
        fragment1.setContentText("函数的定义与性质");
        fragment1.setSummary("函数的定义、定义域、值域等基本概念");
        mockFragments.add(fragment1);
        
        FragmentDTO fragment2 = new FragmentDTO();
        fragment2.setId(2);
        fragment2.setVideoId(1);
        fragment2.setStartTime(120);
        fragment2.setEndTime(240);
        fragment2.setContentText("极限的定义与计算");
        fragment2.setSummary("极限的 ε-δ 定义和基本计算方法");
        mockFragments.add(fragment2);
        
        // 初始化学习记录
        LearningRecord record1 = new LearningRecord();
        record1.setUserId(1);
        record1.setVideoId(1);
        record1.setProgress(50.0);
        record1.setLastWatchTime(300.5);
        record1.setTotalDuration(300);
        record1.setCompleted(false);
        mockLearningRecords.add(record1);
    }

    @Override
    public List<CourseDTO> getCourses(String keyword, int page, int pageSize) {
        // 模拟课程列表查询
        List<CourseDTO> result = new ArrayList<>();
        for (CourseDTO course : mockCourses) {
            if (keyword == null || course.getTitle().contains(keyword)) {
                result.add(course);
            }
        }
        // 模拟分页
        int start = (page - 1) * pageSize;
        int end = Math.min(start + pageSize, result.size());
        if (start < result.size()) {
            return result.subList(start, end);
        }
        return new ArrayList<>();
    }

    @Override
    public CourseDetailDTO getCourseDetail(int courseId) {
        // 模拟课程详情查询
        CourseDTO course = null;
        for (CourseDTO c : mockCourses) {
            if (c.getId() == courseId) {
                course = c;
                break;
            }
        }
        if (course == null) {
            return null;
        }
        
        CourseDetailDTO detail = new CourseDetailDTO();
        detail.setId(course.getId());
        detail.setTitle(course.getTitle());
        detail.setTeacherName(course.getTeacherName());
        detail.setDescription(course.getDescription());
        detail.setCoverImage(course.getCoverImage());
        
        // 查找该课程的视频
        List<VideoDTO> courseVideos = new ArrayList<>();
        for (VideoDTO video : mockVideos) {
            if (video.getCourseId() == courseId) {
                courseVideos.add(video);
            }
        }
        detail.setVideos(courseVideos);
        
        return detail;
    }

    @Override
    public PlayInfoDTO getVideoPlayInfo(int videoId, int userId) {
        // 模拟视频播放信息查询
        VideoDTO video = null;
        for (VideoDTO v : mockVideos) {
            if (v.getId() == videoId) {
                video = v;
                break;
            }
        }
        if (video == null) {
            return null;
        }
        
        PlayInfoDTO playInfo = new PlayInfoDTO();
        playInfo.setVideoId(video.getId());
        playInfo.setTitle(video.getTitle());
        playInfo.setSignUrl("https://oss.zhike.edu.cn/videos/math-01.mp4?OSSAccessKeyId=xxx&Signature=yyy&Expires=zzz");
        playInfo.setDuration(video.getDuration());
        
        // 查找该视频的知识片段
        List<FragmentDTO> videoFragments = new ArrayList<>();
        for (FragmentDTO fragment : mockFragments) {
            if (fragment.getVideoId() == videoId) {
                videoFragments.add(fragment);
            }
        }
        playInfo.setFragments(videoFragments);
        
        // 查找该用户的学习记录
        Double lastWatchTime = null;
        for (LearningRecord record : mockLearningRecords) {
            if (record.getUserId() == userId && record.getVideoId() == videoId) {
                lastWatchTime = record.getLastWatchTime();
                break;
            }
        }
        playInfo.setLastWatchTime(lastWatchTime);
        
        return playInfo;
    }

    @Override
    public void heartbeat(HeartbeatRequest request, int userId) {
        // 模拟心跳上报
        int videoId = request.getVideoId();
        double currentTime = request.getCurrentTime();
        
        // 查找或创建学习记录
        LearningRecord record = null;
        for (LearningRecord r : mockLearningRecords) {
            if (r.getUserId() == userId && r.getVideoId() == videoId) {
                record = r;
                break;
            }
        }
        
        if (record == null) {
            record = new LearningRecord();
            record.setUserId(userId);
            record.setVideoId(videoId);
            mockLearningRecords.add(record);
        }
        
        // 更新学习记录
        record.setLastWatchTime(currentTime);
        record.setTotalDuration(record.getTotalDuration() + 10); // 假设每10秒上报一次
    }

    @Override
    public void saveProgress(ProgressRequest request, int userId) {
        // 模拟进度保存
        int videoId = request.getVideoId();
        double currentTime = request.getCurrentTime();
        int duration = request.getDuration();
        
        // 计算进度百分比
        double progress = (currentTime / duration) * 100;
        
        // 查找或创建学习记录
        LearningRecord record = null;
        for (LearningRecord r : mockLearningRecords) {
            if (r.getUserId() == userId && r.getVideoId() == videoId) {
                record = r;
                break;
            }
        }
        
        if (record == null) {
            record = new LearningRecord();
            record.setUserId(userId);
            record.setVideoId(videoId);
            mockLearningRecords.add(record);
        }
        
        // 更新学习记录
        record.setProgress(progress);
        record.setLastWatchTime(currentTime);
        if (progress >= 95) {
            record.setCompleted(true);
        }
    }

    @Override
    public int addNote(NoteRequest request, int userId) {
        // 模拟添加笔记
        int videoId = request.getVideoId();
        double timestamp = request.getTimestamp();
        String content = request.getContent();
        
        NoteDTO note = new NoteDTO();
        note.setId(mockNotes.size() + 1);
        note.setVideoId(videoId);
        note.setTimestamp(timestamp);
        note.setContent(content);
        note.setPublic(false);
        note.setCreatedAt(java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'+08:00'")));
        
        mockNotes.add(note);
        return note.getId();
    }

    @Override
    public List<NoteDTO> getNotes(int videoId, int userId) {
        // 模拟获取笔记列表
        List<NoteDTO> result = new ArrayList<>();
        for (NoteDTO note : mockNotes) {
            if (note.getVideoId() == videoId) {
                result.add(note);
            }
        }
        return result;
    }
    
    // 模拟学习记录类
    private static class LearningRecord {
        private int userId;
        private int videoId;
        private double progress;
        private double lastWatchTime;
        private int totalDuration;
        private boolean completed;
        
        public int getUserId() {
            return userId;
        }
        public void setUserId(int userId) {
            this.userId = userId;
        }
        public int getVideoId() {
            return videoId;
        }
        public void setVideoId(int videoId) {
            this.videoId = videoId;
        }
        public double getProgress() {
            return progress;
        }
        public void setProgress(double progress) {
            this.progress = progress;
        }
        public double getLastWatchTime() {
            return lastWatchTime;
        }
        public void setLastWatchTime(double lastWatchTime) {
            this.lastWatchTime = lastWatchTime;
        }
        public int getTotalDuration() {
            return totalDuration;
        }
        public void setTotalDuration(int totalDuration) {
            this.totalDuration = totalDuration;
        }
        public boolean isCompleted() {
            return completed;
        }
        public void setCompleted(boolean completed) {
            this.completed = completed;
        }
    }
}