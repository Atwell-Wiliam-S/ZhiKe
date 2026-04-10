package com.zhike.module_rag.service;

import com.zhike.module_rag.dto.RagChatRequest;
import com.zhike.module_rag.dto.QaLogDTO;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

public interface RagChatService {
    // 流式智能问答
    SseEmitter chatStream(RagChatRequest request);

    // 获取问答历史
    List<QaLogDTO> getQaHistory(Integer page, Integer pageSize);
}
