package com.zhike.module_rag.service.impl;

import com.zhike.module_ai_adapter.dto.TextGenerationRequest;
import com.zhike.module_ai_adapter.dto.TextStreamCallback;
import com.zhike.module_ai_adapter.service.AiAdapterService;
import com.zhike.module_rag.dto.ContextFragment;
import com.zhike.module_rag.dto.QaLogDTO;
import com.zhike.module_rag.dto.RagChatRequest;
import com.zhike.module_rag.service.RagChatService;
import com.zhike.module_video.service.VideoAnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class RagChatServiceImpl implements RagChatService {

    @Autowired
    private AiAdapterService aiAdapterService;

    @Autowired
    private VideoAnalysisService videoAnalysisService;

    // 模拟问答历史存储
    private final ConcurrentHashMap<Integer, QaLogDTO> qaLogMap = new ConcurrentHashMap<>();
    private int qaLogIdCounter = 1;

    @Override
    public SseEmitter chatStream(RagChatRequest request) {
        SseEmitter emitter = new SseEmitter(60000L); // 1分钟超时

        // 异步处理流式响应
        new Thread(() -> {
            try {
                // 1. 向量检索相关知识片段
                List<ContextFragment> contextFragments = retrieveRelevantFragments(request.getVideoId(), request.getQuestion());

                // 2. 构建Prompt
                String prompt = buildPrompt(request.getQuestion(), contextFragments);

                // 3. 调用AI模型生成回答
                TextGenerationRequest aiRequest = new TextGenerationRequest();
                aiRequest.setModel("qwen-plus");
                aiRequest.setPrompt(prompt);
                aiRequest.setMaxTokens(1024);
                aiRequest.setTemperature(0.7);
                aiRequest.setStream(true);

                // 4. 处理流式响应
                aiAdapterService.generateTextStream(aiRequest, new TextStreamCallback() {
                    @Override
                    public void onChunk(String chunk) {
                        try {
                            emitter.send("data: " + chunk + "\n\n");
                        } catch (IOException e) {
                            emitter.completeWithError(e);
                        }
                    }

                    @Override
                    public void onError(Throwable error) {
                        emitter.completeWithError(error);
                    }

                    @Override
                    public void onComplete() {
                        try {
                            // 发送知识溯源信息
                            String sourcesJson = "{\"type\":\"source\",\"fragments\":[";
                            for (int i = 0; i < contextFragments.size(); i++) {
                                ContextFragment fragment = contextFragments.get(i);
                                sourcesJson += "{\"fragmentId\":\"" + fragment.getFragmentId() + "\",\"startTime\":\"" + fragment.getStartTime() + "\",\"endTime\":\"" + fragment.getEndTime() + "\",\"relevance\":\"" + fragment.getRelevance() + "\"}";
                                if (i < contextFragments.size() - 1) {
                                    sourcesJson += ",";
                                }
                            }
                            sourcesJson += "]}";
                            emitter.send("data: " + sourcesJson + "\n\n");

                            // 发送完成信息
                            emitter.send("data: {\"type\":\"done\",\"tokenUsage\":256}\n\n");
                            emitter.complete();

                            // 保存问答历史
                            saveQaLog(request.getQuestion(), "生成的回答", contextFragments);
                        } catch (IOException e) {
                            emitter.completeWithError(e);
                        }
                    }
                });
            } catch (Exception e) {
                emitter.completeWithError(e);
            }
        }).start();

        return emitter;
    }

    @Override
    public List<QaLogDTO> getQaHistory(Integer page, Integer pageSize) {
        List<QaLogDTO> qaLogs = new ArrayList<>(qaLogMap.values());
        // 模拟分页
        int start = (page - 1) * pageSize;
        int end = Math.min(start + pageSize, qaLogs.size());
        if (start >= qaLogs.size()) {
            return new ArrayList<>();
        }
        return qaLogs.subList(start, end);
    }

    // 模拟向量检索相关知识片段
    private List<ContextFragment> retrieveRelevantFragments(Integer videoId, String question) {
        List<ContextFragment> fragments = new ArrayList<>();

        // 模拟检索到的相关片段
        ContextFragment fragment1 = new ContextFragment();
        fragment1.setFragmentId(1);
        fragment1.setStartTime(120.0);
        fragment1.setEndTime(180.0);
        fragment1.setRelevance(0.95);
        fragments.add(fragment1);

        ContextFragment fragment2 = new ContextFragment();
        fragment2.setFragmentId(2);
        fragment2.setStartTime(240.0);
        fragment2.setEndTime(300.0);
        fragment2.setRelevance(0.85);
        fragments.add(fragment2);

        return fragments;
    }

    // 构建Prompt
    private String buildPrompt(String question, List<ContextFragment> fragments) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("你是一个智能教学助手，根据以下知识片段回答学生的问题。");
        prompt.append("\n\n知识片段：");

        for (ContextFragment fragment : fragments) {
            prompt.append("\n[片段ID: " + fragment.getFragmentId() + ", 时间: " + fragment.getStartTime() + "-" + fragment.getEndTime() + "秒]");
            prompt.append(" 这是知识片段的内容，包含了相关的知识点。");
        }

        prompt.append("\n\n学生问题：" + question);
        prompt.append("\n\n请根据知识片段回答问题，不要添加无关内容。");

        return prompt.toString();
    }

    // 保存问答历史
    private void saveQaLog(String question, String answer, List<ContextFragment> contextFragments) {
        QaLogDTO qaLog = new QaLogDTO();
        qaLog.setId(qaLogIdCounter++);
        qaLog.setQuestion(question);
        qaLog.setAnswer(answer);
        qaLog.setContextFragments(contextFragments);
        qaLog.setModelVersion("qwen-plus");
        qaLog.setTokenUsage(256);
        qaLog.setCreatedAt("2026-04-09 12:00:00");

        qaLogMap.put(qaLog.getId(), qaLog);
    }
}
