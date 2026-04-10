package com.zhike.module_rag.service;

import com.zhike.module_rag.model.QaLog;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.Map;

public interface RagService {

    SseEmitter chatStream(Long userId, String question, Long videoId, Double currentTime);

    Map<String, Object> getHistory(Long userId, Integer page, Integer pageSize);
}
