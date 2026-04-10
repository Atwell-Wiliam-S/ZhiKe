package com.zhike.module_student.service.impl;

import com.zhike.module_student.dto.*;
import com.zhike.module_student.service.StudentService;
import com.zhike.module_video.service.VideoAnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class StudentServiceImpl implements StudentService {

    @Autowired
    private VideoAnalysisService videoAnalysisService;

    // 模拟课程数据
    private final Map<Integer, CourseInfo> courseMap = new HashMap<>();
    // 模拟学习记录
    private final Map<String, LearningRecord> learningRecordMap = new ConcurrentHashMap<>();
    // 模拟笔记数据
    private final Map<Integer, NoteInfo> noteMap = new ConcurrentHashMap<>();
    private int noteIdCounter = 1;

    public StudentServiceImpl() {
        // 初始化课程数据
        CourseInfo course1 = new CourseInfo();
        course1.setId(1);
        course1.setTitle("人工智能基础");
        course1.setDescription("人工智能的基本概念和应用");
        course1.setCoverImage("http://example.com/cover1.jpg");
        course1.setCategory("计算机科学");
        course1.setTeacherName("张教授");
        course1.setTotalVideos(5);
        courseMap.put(1, course1);

        CourseInfo course2 = new CourseInfo();
        course2.setId(2);
        course2.setTitle("机器学习入门");
        course2.setDescription("机器学习的基本算法和实践");
        course2.setCoverImage("http://example.com/cover2.jpg");
        course2.setCategory("计算机科学");
        course2.setTeacherName("李教授");
        course2.setTotalVideos(8);
        courseMap.put(2, course2);
    }

    @Override
    public List<CourseDTO> getCourseList(String keyword, Integer page, Integer pageSize) {
        List<CourseDTO> courseDTOs = new ArrayList<>();
        for (CourseInfo course : courseMap.values()) {
            if (keyword == null || course.getTitle().contains(keyword) || course.getDescription().contains(keyword)) {
                CourseDTO courseDTO = new CourseDTO();
                courseDTO.setId(course.getId());
                courseDTO.setTitle(course.getTitle());
                courseDTO.setDescription(course.getDescription());
                courseDTO.setCoverImage(course.getCoverImage());
                courseDTO.setCategory(course.getCategory());
                courseDTO.setTeacherName(course.getTeacherName());
                courseDTO.setTotalVideos(course.getTotalVideos());
                courseDTO.setCompletionRate(getCompletionRate(course.getId()));
                courseDTOs.add(courseDTO);
            }
        }

        // 模拟分页
        int start = (page - 1) * pageSize;
        int end = Math.min(start + pageSize, courseDTOs.size());
        if (start >= courseDTOs.size()) {
            return new ArrayList<>();
        }
        return courseDTOs.subList(start, end);
    }

    @Override
    public CourseDetailDTO getCourseDetail(Integer courseId) {
        CourseInfo course = courseMap.get(courseId);
        if (course == null) {
            throw new RuntimeException("课程不存在");
        }

        CourseDetailDTO courseDetailDTO = new CourseDetailDTO();
        courseDetailDTO.setId(course.getId());
        courseDetailDTO.setTitle(course.getTitle());
        courseDetailDTO.setDescription(course.getDescription());
        courseDetailDTO.setCoverImage(course.getCoverImage());
        courseDetailDTO.setCategory(course.getCategory());
        courseDetailDTO.setTeacherName(course.getTeacherName());

        // 模拟视频列表
        List<VideoDTO> videos = new ArrayList<>();
        for (int i = 1; i <= course.getTotalVideos(); i++) {
            VideoDTO video = new VideoDTO();
            video.setId(i);
            video.setTitle("第" + i + "讲: 课程内容" + i);
            video.setDuration(600); // 10分钟
            video.setThumbnailUrl("http://example.com/thumbnail" + i + ".jpg");
            video.setOrderNum(i);
            video.setProgress(getVideoProgress(courseId, i));
            video.setIsCompleted(video.getProgress() >= 100);
            videos.add(video);
        }
        courseDetailDTO.setVideos(videos);

        return courseDetailDTO;
    }

    @Override
    public PlayInfoDTO getPlayInfo(Integer videoId) {
        PlayInfoDTO playInfoDTO = new PlayInfoDTO();
        playInfoDTO.setVideoId(videoId);
        playInfoDTO.setTitle("视频标题" + videoId);
        playInfoDTO.setSignUrl("http://example.com/video" + videoId + "?sign=xxx");
        playInfoDTO.setDuration(600); // 10分钟

        // 模拟知识片段
        List<FragmentDTO> fragments = new ArrayList<>();
        for (int i = 1; i <= 3; i++) {
            FragmentDTO fragment = new FragmentDTO();
            fragment.setId(i);
            fragment.setStartTime((i - 1) * 120.0); // 每2分钟一个片段
            fragment.setEndTime(i * 120.0);
            fragment.setSummary("知识片段" + i + "摘要");
            fragments.add(fragment);
        }
        playInfoDTO.setFragments(fragments);

        // 获取上次观看时间
        playInfoDTO.setLastWatchTime(getLastWatchTime(videoId));

        return playInfoDTO;
    }

    @Override
    public void reportHeartbeat(HeartbeatRequest request) {
        // 模拟心跳上报处理
        System.out.println("心跳上报: videoId=" + request.getVideoId() + ", currentTime=" + request.getCurrentTime() + ", status=" + request.getStatus());
    }

    @Override
    public void saveProgress(ProgressRequest request) {
        // 保存学习进度
        String key = "user_1_video_" + request.getVideoId(); // 模拟用户ID为1
        LearningRecord record = learningRecordMap.get(key);
        if (record == null) {
            record = new LearningRecord();
            record.setVideoId(request.getVideoId());
        }
        record.setCurrentTime(request.getCurrentTime());
        record.setDuration(request.getDuration());
        record.setProgress((request.getCurrentTime() / request.getDuration()) * 100);
        learningRecordMap.put(key, record);
    }

    @Override
    public Integer addNote(NoteRequest request) {
        // 添加笔记
        NoteInfo note = new NoteInfo();
        note.setId(noteIdCounter++);
        note.setVideoId(request.getVideoId());
        note.setTimestamp(request.getTimestamp());
        note.setContent(request.getContent());
        note.setCreatedAt("2026-04-09 12:00:00");
        noteMap.put(note.getId(), note);
        return note.getId();
    }

    @Override
    public List<NoteDTO> getNoteList(Integer videoId) {
        List<NoteDTO> noteDTOs = new ArrayList<>();
        for (NoteInfo note : noteMap.values()) {
            if (note.getVideoId().equals(videoId)) {
                NoteDTO noteDTO = new NoteDTO();
                noteDTO.setId(note.getId());
                noteDTO.setVideoId(note.getVideoId());
                noteDTO.setTimestamp(note.getTimestamp());
                noteDTO.setContent(note.getContent());
                noteDTO.setCreatedAt(note.getCreatedAt());
                noteDTOs.add(noteDTO);
            }
        }
        return noteDTOs;
    }

    // 模拟获取课程完成率
    private Double getCompletionRate(Integer courseId) {
        // 模拟数据
        return 0.6; // 60%
    }

    // 模拟获取视频进度
    private Double getVideoProgress(Integer courseId, Integer videoId) {
        String key = "user_1_video_" + videoId;
        LearningRecord record = learningRecordMap.get(key);
        return record != null ? record.getProgress() : 0.0;
    }

    // 模拟获取上次观看时间
    private Double getLastWatchTime(Integer videoId) {
        String key = "user_1_video_" + videoId;
        LearningRecord record = learningRecordMap.get(key);
        return record != null ? record.getCurrentTime() : null;
    }

    // 内部类：课程信息
    private static class CourseInfo {
        private Integer id;
        private String title;
        private String description;
        private String coverImage;
        private String category;
        private String teacherName;
        private Integer totalVideos;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getCoverImage() {
            return coverImage;
        }

        public void setCoverImage(String coverImage) {
            this.coverImage = coverImage;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public String getTeacherName() {
            return teacherName;
        }

        public void setTeacherName(String teacherName) {
            this.teacherName = teacherName;
        }

        public Integer getTotalVideos() {
            return totalVideos;
        }

        public void setTotalVideos(Integer totalVideos) {
            this.totalVideos = totalVideos;
        }
    }

    // 内部类：学习记录
    private static class LearningRecord {
        private Integer videoId;
        private Double currentTime;
        private Integer duration;
        private Double progress;

        public Integer getVideoId() {
            return videoId;
        }

        public void setVideoId(Integer videoId) {
            this.videoId = videoId;
        }

        public Double getCurrentTime() {
            return currentTime;
        }

        public void setCurrentTime(Double currentTime) {
            this.currentTime = currentTime;
        }

        public Integer getDuration() {
            return duration;
        }

        public void setDuration(Integer duration) {
            this.duration = duration;
        }

        public Double getProgress() {
            return progress;
        }

        public void setProgress(Double progress) {
            this.progress = progress;
        }
    }

    // 内部类：笔记信息
    private static class NoteInfo {
        private Integer id;
        private Integer videoId;
        private Double timestamp;
        private String content;
        private String createdAt;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public Integer getVideoId() {
            return videoId;
        }

        public void setVideoId(Integer videoId) {
            this.videoId = videoId;
        }

        public Double getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(Double timestamp) {
            this.timestamp = timestamp;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }
    }
}
