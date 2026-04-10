package com.zhike.module_ai_adapter.service.impl;

import com.zhike.common.exception.BusinessException;
import com.zhike.module_ai_adapter.dto.*;
import com.zhike.module_ai_adapter.service.AiAdapterService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class AiAdapterServiceImpl implements AiAdapterService {

    @Value("${alibaba.dashscope.api-key}")
    private String apiKey;

    @Override
    public TextGenerationResponse generateText(TextGenerationRequest request) {
        // 模拟文本生成响应
        TextGenerationResponse response = new TextGenerationResponse();
        response.setId(UUID.randomUUID().toString());
        response.setObject("text_completion");
        response.setCreated(System.currentTimeMillis() / 1000);
        response.setModel(request.getModel());

        Choice choice = new Choice();
        choice.setIndex(0);
        Message message = new Message();
        message.setRole("assistant");
        message.setContent("这是一个模拟的文本生成结果，基于你的提示：" + request.getPrompt());
        choice.setMessage(message);
        choice.setFinishReason("stop");
        response.setChoices(new Choice[]{choice});

        Usage usage = new Usage();
        usage.setPromptTokens(10);
        usage.setCompletionTokens(20);
        usage.setTotalTokens(30);
        response.setUsage(usage);

        return response;
    }

    @Override
    public VisualAnalysisResponse analyzeVisual(VisualAnalysisRequest request) {
        // 模拟视觉分析响应
        VisualAnalysisResponse response = new VisualAnalysisResponse();
        response.setId(UUID.randomUUID().toString());
        response.setObject("visual_analysis");
        response.setCreated(System.currentTimeMillis() / 1000);
        response.setModel(request.getModel());
        response.setAnalysisResult("这是一个模拟的视觉分析结果，基于图片：" + request.getImageUrl());
        response.setTags(new String[]{"教育", "学习", "视频"});

        return response;
    }

    @Override
    public SpeechToTextResponse transcribeSpeech(SpeechToTextRequest request) {
        // 模拟语音转写响应
        SpeechToTextResponse response = new SpeechToTextResponse();
        response.setId(UUID.randomUUID().toString());
        response.setObject("speech_transcription");
        response.setCreated(System.currentTimeMillis() / 1000);
        response.setModel(request.getModel());
        response.setText("这是一个模拟的语音转写结果，基于音频：" + request.getAudioUrl());

        // 模拟单词级别的转写结果
        Word word1 = new Word();
        word1.setWord("这是");
        word1.setStartTime(0.0);
        word1.setEndTime(0.5);

        Word word2 = new Word();
        word2.setWord("一个");
        word2.setStartTime(0.5);
        word2.setEndTime(1.0);

        response.setWords(new Word[]{word1, word2});

        return response;
    }

    @Override
    public EmbeddingResponse embedText(EmbeddingRequest request) {
        // 模拟文本向量化响应
        EmbeddingResponse response = new EmbeddingResponse();
        response.setId(UUID.randomUUID().toString());
        response.setObject("embedding");
        response.setCreated(System.currentTimeMillis() / 1000);
        response.setModel(request.getModel());

        List<Embedding> embeddings = new ArrayList<>();
        for (int i = 0; i < request.getInput().size(); i++) {
            Embedding embedding = new Embedding();
            embedding.setIndex(i);
            embedding.setObject("embedding");
            // 生成随机向量
            List<Double> vector = new ArrayList<>();
            for (int j = 0; j < 768; j++) {
                vector.add(Math.random() * 2 - 1);
            }
            embedding.setEmbedding(vector);
            embeddings.add(embedding);
        }
        response.setData(embeddings);

        Usage usage = new Usage();
        usage.setPromptTokens(50);
        usage.setTotalTokens(50);
        response.setUsage(usage);

        return response;
    }

    @Override
    public void generateTextStream(TextGenerationRequest request, TextStreamCallback callback) {
        try {
            // 模拟流式生成
            String responseText = "这是一个模拟的流式文本生成结果，基于你的提示：" + request.getPrompt();
            for (int i = 0; i < responseText.length(); i += 2) {
                int end = Math.min(i + 2, responseText.length());
                String chunk = responseText.substring(i, end);
                callback.onChunk(chunk);
                // 模拟延迟
                Thread.sleep(100);
            }
            callback.onComplete();
        } catch (Exception e) {
            callback.onError(e);
        }
    }
}
