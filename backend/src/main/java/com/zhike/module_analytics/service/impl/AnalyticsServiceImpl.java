package com.zhike.module_analytics.service.impl;

import com.zhike.module_analytics.mapper.LearningRecordMapper;
import com.zhike.module_analytics.model.LearningRecord;
import com.zhike.module_analytics.service.AnalyticsService;
import com.zhike.module_rag.mapper.QaLogMapper;
import com.zhike.module_rag.model.QaLog;
import com.zhike.module_video.mapper.VideoMapper;
import com.zhike.module_video.model.Video;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 学情分析服务实现类
 * 实现学习行为数据的统计分析功能
 * 注意：当前为模拟实现，实际应基于真实学习记录数据进行计算
 */
@Service
public class AnalyticsServiceImpl implements AnalyticsService {

    @Autowired
    private LearningRecordMapper learningRecordMapper;

    @Autowired
    private VideoMapper videoMapper;

    @Autowired
    private QaLogMapper qaLogMapper;

    /**
     * 获取视频观看热力图数据
     * 分析学生在视频不同时间段的观看行为
     * 注意：当前为模拟实现，使用随机数据生成热力图
     * 
     * @param courseId 课程ID
     * @param videoId 视频ID
     * @return 包含热力图数据的Map
     */
    @Override
    public Map<String, Object> getHeatmap(Long courseId, Long videoId) {
        Video video = videoMapper.findById(videoId);
        List<LearningRecord> records = learningRecordMapper.findByVideoId(videoId);

        Map<String, Object> heatmap = new HashMap<>();
        heatmap.put("videoId", videoId);
        heatmap.put("duration", video.getDuration());
        heatmap.put("windowSize", 30);

        List<Map<String, Object>> data = new ArrayList<>();
        for (int i = 0; i < video.getDuration() / 30; i++) {
            Map<String, Object> point = new HashMap<>();
            point.put("startTime", i * 30);
            point.put("endTime", (i + 1) * 30);
            point.put("score", Math.random());
            point.put("isDifficulty", Math.random() > 0.7);
            data.add(point);
        }
        heatmap.put("data", data);

        return heatmap;
    }

    /**
     * 获取班级学习概览
     * 统计班级整体的学习情况数据
     * 注意：当前为模拟实现，使用硬编码数据
     * 
     * @param courseId 课程ID
     * @return 包含班级概览数据的Map
     */
    @Override
    public Map<String, Object> getClassOverview(Long courseId) {
        List<LearningRecord> records = learningRecordMapper.findByCourseId(courseId);

        Map<String, Object> overview = new HashMap<>();
        overview.put("totalStudents", records.size());
        overview.put("avgCompletionRate", 0.72);
        overview.put("avgWatchDuration", 2850);
        overview.put("activeTodayCount", 89);
        overview.put("totalQuestions", 1243);
        overview.put("difficultyPoints", 5);

        return overview;
    }

    /**
     * 获取学生个人画像
     * 分析学生的学习行为、进度、薄弱点等
     * 注意：当前为模拟实现，使用硬编码和模拟数据
     * 
     * @param studentId 学生ID
     * @return 包含学生画像数据的Map
     */
    @Override
    public Map<String, Object> getStudentProfile(Long studentId) {
        List<LearningRecord> records = learningRecordMapper.findByUserId(studentId);

        Map<String, Object> profile = new HashMap<>();
        profile.put("userId", studentId);
        profile.put("username", "学生" + studentId);
        profile.put("totalWatchDuration", 12500);
        profile.put("completionRate", 0.85);
        profile.put("questionCount", 45);

        List<String> weakPoints = new ArrayList<>();
        weakPoints.add("傅里叶变换");
        weakPoints.add("线性代数");
        profile.put("weakPoints", weakPoints);

        List<Map<String, Object>> learningTrend = new ArrayList<>();
        for (int i = 7; i > 0; i--) {
            Map<String, Object> trendPoint = new HashMap<>();
            trendPoint.put("date", "2024-01-" + (15 - i));
            trendPoint.put("watchDuration", 1200 + i * 100);
            trendPoint.put("questionCount", 3 + i);
            learningTrend.add(trendPoint);
        }
        profile.put("learningTrend", learningTrend);

        return profile;
    }

    /**
     * 获取学习关键词词云
     * 分析指定时间段内学生提问和学习内容中的关键词
     * 注意：当前为模拟实现，使用预定义的关键词和随机数据
     * 
     * @param courseId 课程ID
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 包含词云数据的Map
     */
    @Override
    public Map<String, Object> getWordcloud(Long courseId, String startDate, String endDate) {
        Map<String, Object> wordcloud = new HashMap<>();
        wordcloud.put("courseId", courseId);

        List<Map<String, Object>> words = new ArrayList<>();
        String[] wordList = {"傅里叶变换", "线性代数", "微积分", "概率统计", "矩阵", "向量", "导数", "积分"};
        for (String word : wordList) {
            Map<String, Object> wordItem = new HashMap<>();
            wordItem.put("text", word);
            wordItem.put("count", (int) (Math.random() * 100) + 10);
            wordItem.put("weight", Math.random());
            words.add(wordItem);
        }
        wordcloud.put("words", words);

        return wordcloud;
    }
}
