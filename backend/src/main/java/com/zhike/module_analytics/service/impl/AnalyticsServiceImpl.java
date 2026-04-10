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

@Service
public class AnalyticsServiceImpl implements AnalyticsService {

    @Autowired
    private LearningRecordMapper learningRecordMapper;

    @Autowired
    private VideoMapper videoMapper;

    @Autowired
    private QaLogMapper qaLogMapper;

    @Override
    public Map<String, Object> getHeatmap(Long courseId, Long videoId) {
        Video video = videoMapper.findById(videoId);
        List<LearningRecord> records = learningRecordMapper.findByVideoId(videoId);

        Map<String, Object> heatmap = new HashMap<>();
        heatmap.put("videoId", videoId);
        heatmap.put("duration", video.getDuration());
        heatmap.put("windowSize", 30);

        // 模拟生成热力图数据
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
