package com.zhike.module_ai_adapter.service;

import com.zhike.module_ai_adapter.dto.*;

public interface AiAdapterService {
    // 文本生成
    TextGenerationResponse generateText(TextGenerationRequest request);

    // 视觉分析
    VisualAnalysisResponse analyzeVisual(VisualAnalysisRequest request);

    // 语音转写
    SpeechToTextResponse transcribeSpeech(SpeechToTextRequest request);

    // 文本向量化
    EmbeddingResponse embedText(EmbeddingRequest request);

    // 流式文本生成
    void generateTextStream(TextGenerationRequest request, TextStreamCallback callback);
}
