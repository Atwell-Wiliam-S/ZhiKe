package com.zhike.module_rag.service.impl;

import com.zhike.module_rag.mapper.QaLogMapper;
import com.zhike.module_rag.model.QaLog;
import com.zhike.module_rag.service.RagService;
import com.zhike.module_video.mapper.KnowledgeFragmentMapper;
import com.zhike.module_video.model.KnowledgeFragment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RagServiceImpl implements RagService {

    @Autowired
    private QaLogMapper qaLogMapper;

    @Autowired
    private KnowledgeFragmentMapper knowledgeFragmentMapper;

    @Override
    public SseEmitter chatStream(Long userId, String question, Long videoId, Double currentTime) {
        SseEmitter emitter = new SseEmitter();

        // 异步处理SSE流式响应
        new Thread(() -> {
            try {
                // 1. 检索相关知识片段
                List<KnowledgeFragment> fragments = knowledgeFragmentMapper.findByVideoId(videoId);
                
                // 2. 模拟AI生成回答
                String[] answerChunks = {"傅里叶变换", "的物理意义", "是将信号从时域转换到", "频域进行分析，", "便于处理和理解信号的频率特性。"};
                
                // 3. 发送文本片段
                for (String chunk : answerChunks) {
                    Map<String, Object> data = new HashMap<>();
                    data.put("type", "chunk");
                    data.put("content", chunk);
                    emitter.send(SseEmitter.event().name("message").data(data));
                    Thread.sleep(500); // 模拟流式效果
                }
                
                // 4. 发送参考来源
                List<Map<String, Object>> contextFragments = new ArrayList<>();
                for (int i = 0; i < Math.min(2, fragments.size()); i++) {
                    KnowledgeFragment fragment = fragments.get(i);
                    Map<String, Object> contextFragment = new HashMap<>();
                    contextFragment.put("fragmentId", fragment.getId());
                    contextFragment.put("startTime", fragment.getStartTime());
                    contextFragment.put("endTime", fragment.getEndTime());
                    contextFragment.put("relevance", 0.9 - i * 0.1);
                    contextFragments.add(contextFragment);
                }
                Map<String, Object> sourceData = new HashMap<>();
                sourceData.put("type", "source");
                sourceData.put("fragments", contextFragments);
                emitter.send(SseEmitter.event().name("message").data(sourceData));
                
                // 5. 发送完成状态
                Map<String, Object> doneData = new HashMap<>();
                doneData.put("type", "done");
                doneData.put("tokenUsage", 256);
                emitter.send(SseEmitter.event().name("message").data(doneData));
                
                // 6. 保存问答日志
                QaLog qaLog = new QaLog();
                qaLog.setUserId(userId);
                qaLog.setVideoId(videoId);
                qaLog.setQuestion(question);
                qaLog.setAnswer(String.join("", answerChunks));
                qaLog.setContextFragments("[]"); // 这里应该序列化contextFragments
                qaLog.setModelVersion("gpt-3.5-turbo");
                qaLog.setTokenUsage(256);
                qaLog.setCreatedAt(LocalDateTime.now());
                qaLog.setUpdatedAt(LocalDateTime.now());
                qaLogMapper.insert(qaLog);
                
                emitter.complete();
            } catch (Exception e) {
                try {
                    Map<String, Object> errorData = new HashMap<>();
                    errorData.put("type", "error");
                    errorData.put("message", "生成回答失败");
                    errorData.put("code", 500);
                    emitter.send(SseEmitter.event().name("message").data(errorData));
                    emitter.completeWithError(e);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }).start();

        return emitter;
    }

    @Override
    public Map<String, Object> getHistory(Long userId, Integer page, Integer pageSize) {
        Integer offset = (page - 1) * pageSize;
        List<QaLog> logs = qaLogMapper.findByUserId(userId, pageSize, offset);
        Integer total = qaLogMapper.countByUserId(userId);
        Integer totalPages = (total + pageSize - 1) / pageSize;

        Map<String, Object> result = new HashMap<>();
        result.put("records", logs);
        result.put("total", total);
        result.put("page", page);
        result.put("pageSize", pageSize);
        result.put("totalPages", totalPages);

        return result;
    }
}
