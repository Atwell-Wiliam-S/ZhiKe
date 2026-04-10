package com.zhike.module_student.service.impl;

import com.zhike.common.util.PageResult;
import com.zhike.module_student.model.Course;
import com.zhike.module_student.model.Note;
import com.zhike.module_student.service.StudentService;
import com.zhike.module_video.model.KnowledgeFragment;
import com.zhike.module_video.model.Video;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 学生服务实现类
 * 实现学生端的课程学习、视频播放、笔记管理等功能
 * 注意：当前为模拟实现，使用内存中的模拟数据
 */
@Service
public class StudentServiceImpl implements StudentService {

    private List<Course> mockCourses = new ArrayList<>();
    
    private List<Video> mockVideos = new ArrayList<>();
    
    private List<KnowledgeFragment> mockFragments = new ArrayList<>();
    
    private List<Note> mockNotes = new ArrayList<>();
    
    private List<LearningRecord> mockLearningRecords = new ArrayList<>();
    
    /**
     * 构造函数，初始化模拟数据
     */
    public StudentServiceImpl() {
        Course course1 = new Course();
        course1.setId(1L);
        course1.setTitle("高等数学 A");
        course1.setTeacherName("李教授");
        course1.setDescription("涵盖微积分、线性代数等内容");
        course1.setCoverImage("/covers/math-a.jpg");
        course1.setVideoCount(24);
        course1.setPublishedAt(java.time.LocalDateTime.now().minusMonths(1));
        mockCourses.add(course1);
        
        Course course2 = new Course();
        course2.setId(2L);
        course2.setTitle("大学物理");
        course2.setTeacherName("王教授");
        course2.setDescription("涵盖力学、热学、电磁学等内容");
        course2.setCoverImage("/covers/physics.jpg");
        course2.setVideoCount(18);
        course2.setPublishedAt(java.time.LocalDateTime.now().minusWeeks(2));
        mockCourses.add(course2);
        
        Video video1 = new Video();
        video1.setId(1L);
        video1.setCourseId(1L);
        video1.setTitle("第一章：函数与极限");
        video1.setOssUrl("https://oss.zhike.edu.cn/videos/math-01.mp4");
        video1.setDuration(600);
        video1.setParseStatus(2);
        video1.setThumbnailUrl("/thumbnails/math-01.jpg");
        video1.setOrderNum(1);
        mockVideos.add(video1);
        
        Video video2 = new Video();
        video2.setId(2L);
        video2.setCourseId(1L);
        video2.setTitle("第二章：导数与微分");
        video2.setOssUrl("https://oss.zhike.edu.cn/videos/math-02.mp4");
        video2.setDuration(720);
        video2.setParseStatus(2);
        video2.setThumbnailUrl("/thumbnails/math-02.jpg");
        video2.setOrderNum(2);
        mockVideos.add(video2);
        
        KnowledgeFragment fragment1 = new KnowledgeFragment();
        fragment1.setId(1L);
        fragment1.setVideoId(1L);
        fragment1.setStartTime(0.0);
        fragment1.setEndTime(120.0);
        fragment1.setContentText("函数的定义与性质");
        fragment1.setSummary("函数的定义、定义域、值域等基本概念");
        mockFragments.add(fragment1);
        
        KnowledgeFragment fragment2 = new KnowledgeFragment();
        fragment2.setId(2L);
        fragment2.setVideoId(1L);
        fragment2.setStartTime(120.0);
        fragment2.setEndTime(240.0);
        fragment2.setContentText("极限的定义与计算");
        fragment2.setSummary("极限的 ε-δ 定义和基本计算方法");
        mockFragments.add(fragment2);
        
        LearningRecord record1 = new LearningRecord();
        record1.setUserId(1L);
        record1.setVideoId(1L);
        record1.setProgress(50.0);
        record1.setLastWatchTime(300.5);
        record1.setTotalDuration(300);
        record1.setCompleted(false);
        mockLearningRecords.add(record1);
    }

    /**
     * 分页查询课程列表
     * 
     * @param keyword 搜索关键词
     * @param page 页码
     * @param pageSize 每页数量
     * @return 分页结果，包含课程列表
     */
    @Override
    public PageResult<Course> getCourses(String keyword, Integer page, Integer pageSize) {
        List<Course> result = new ArrayList<>();
        for (Course course : mockCourses) {
            if (keyword == null || course.getTitle().contains(keyword)) {
                result.add(course);
            }
        }
        
        int start = (page - 1) * pageSize;
        int end = Math.min(start + pageSize, result.size());
        List<Course> pageResult = new ArrayList<>();
        if (start < result.size()) {
            pageResult = result.subList(start, end);
        }
        
        PageResult<Course> pageResultObj = new PageResult<>();
        pageResultObj.setRecords(pageResult);
        pageResultObj.setTotal(result.size());
        pageResultObj.setPage(page);
        pageResultObj.setPageSize(pageSize);
        pageResultObj.setTotalPages((int) Math.ceil((double) result.size() / pageSize));
        
        return pageResultObj;
    }

    /**
     * 获取课程详情
     * 
     * @param courseId 课程ID
     * @return 包含课程详情和视频列表的Map
     */
    @Override
    public Map<String, Object> getCourseDetail(Long courseId) {
        Course course = null;
        for (Course c : mockCourses) {
            if (c.getId().equals(courseId)) {
                course = c;
                break;
            }
        }
        if (course == null) {
            return null;
        }
        
        List<Video> courseVideos = new ArrayList<>();
        for (Video video : mockVideos) {
            if (video.getCourseId().equals(courseId)) {
                courseVideos.add(video);
            }
        }
        
        Map<String, Object> result = new HashMap<>();
        result.put("course", course);
        result.put("videos", courseVideos);
        
        return result;
    }

    /**
     * 获取视频播放信息
     * 
     * @param videoId 视频ID
     * @param userId 用户ID
     * @return 包含播放信息的Map
     */
    @Override
    public Map<String, Object> getPlayInfo(Long videoId, Long userId) {
        Video video = null;
        for (Video v : mockVideos) {
            if (v.getId().equals(videoId)) {
                video = v;
                break;
            }
        }
        if (video == null) {
            return null;
        }
        
        List<KnowledgeFragment> videoFragments = new ArrayList<>();
        for (KnowledgeFragment fragment : mockFragments) {
            if (fragment.getVideoId().equals(videoId)) {
                videoFragments.add(fragment);
            }
        }
        
        Double lastWatchTime = null;
        for (LearningRecord record : mockLearningRecords) {
            if (record.getUserId().equals(userId) && record.getVideoId().equals(videoId)) {
                lastWatchTime = record.getLastWatchTime();
                break;
            }
        }
        
        Map<String, Object> result = new HashMap<>();
        result.put("video", video);
        result.put("signUrl", "https://oss.zhike.edu.cn/videos/math-01.mp4?OSSAccessKeyId=xxx&Signature=yyy&Expires=zzz");
        result.put("fragments", videoFragments);
        result.put("lastWatchTime", lastWatchTime);
        
        return result;
    }

    /**
     * 视频观看心跳上报
     * 
     * @param userId 用户ID
     * @param videoId 视频ID
     * @param currentTime 当前观看时间点
     * @param status 观看状态
     */
    @Override
    public void heartbeat(Long userId, Long videoId, Double currentTime, String status) {
        LearningRecord record = null;
        for (LearningRecord r : mockLearningRecords) {
            if (r.getUserId().equals(userId) && r.getVideoId().equals(videoId)) {
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
        
        record.setLastWatchTime(currentTime);
        record.setTotalDuration(record.getTotalDuration() + 10);
    }

    /**
     * 保存视频观看进度
     * 
     * @param userId 用户ID
     * @param videoId 视频ID
     * @param currentTime 当前观看时间点
     * @param duration 视频总时长
     */
    @Override
    public void saveProgress(Long userId, Long videoId, Double currentTime, Integer duration) {
        double progress = (currentTime / duration) * 100;
        
        LearningRecord record = null;
        for (LearningRecord r : mockLearningRecords) {
            if (r.getUserId().equals(userId) && r.getVideoId().equals(videoId)) {
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
        
        record.setProgress(progress);
        record.setLastWatchTime(currentTime);
        if (progress >= 95) {
            record.setCompleted(true);
        }
    }

    /**
     * 添加学习笔记
     * 
     * @param userId 用户ID
     * @param videoId 视频ID
     * @param timestamp 笔记对应的时间点
     * @param content 笔记内容
     * @return 创建的笔记ID
     */
    @Override
    public Long addNote(Long userId, Long videoId, Double timestamp, String content) {
        Note note = new Note();
        note.setId((long) (mockNotes.size() + 1));
        note.setUserId(userId);
        note.setVideoId(videoId);
        note.setTimestamp(timestamp);
        note.setContent(content);
        note.setIsPublic(0);
        note.setCreatedAt(java.time.LocalDateTime.now());
        
        mockNotes.add(note);
        return note.getId();
    }

    /**
     * 获取视频的笔记列表
     * 
     * @param userId 用户ID
     * @param videoId 视频ID
     * @return 包含笔记列表的Map
     */
    @Override
    public Map<String, Object> getNotes(Long userId, Long videoId) {
        List<Note> result = new ArrayList<>();
        for (Note note : mockNotes) {
            if (note.getVideoId().equals(videoId)) {
                result.add(note);
            }
        }
        
        Map<String, Object> response = new HashMap<>();
        response.put("notes", result);
        
        return response;
    }
    
    /**
     * 模拟学习记录内部类
     */
    private static class LearningRecord {
        private Long userId;
        private Long videoId;
        private double progress;
        private double lastWatchTime;
        private int totalDuration;
        private boolean completed;
        
        public Long getUserId() {
            return userId;
        }
        public void setUserId(Long userId) {
            this.userId = userId;
        }
        public Long getVideoId() {
            return videoId;
        }
        public void setVideoId(Long videoId) {
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
