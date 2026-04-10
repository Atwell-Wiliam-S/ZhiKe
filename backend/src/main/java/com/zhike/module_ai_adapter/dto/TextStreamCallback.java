package com.zhike.module_ai_adapter.dto;

public interface TextStreamCallback {
    void onChunk(String chunk);
    void onError(Throwable error);
    void onComplete();
}
