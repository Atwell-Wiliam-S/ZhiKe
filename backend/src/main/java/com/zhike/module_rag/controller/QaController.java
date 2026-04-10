package com.zhike.module_rag.controller;

import com.zhike.common.result.Result;
import com.zhike.module_rag.dto.QaLogDTO;
import com.zhike.module_rag.dto.RagChatRequest;
import com.zhike.module_rag.service.RagChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

@RestController
@RequestMapping("/api/qa")
public class QaController {

    @Autowired
    private RagChatService ragChatService;

    // 流式智能问答
    @PostMapping("/chat-stream")
    public SseEmitter chatStream(@RequestBody RagChatRequest request) {
        return ragChatService.chatStream(request);
    }

    // 获取问答历史
    @GetMapping("/history")
    public Result<List<QaLogDTO>> getQaHistory(@RequestParam(defaultValue = "1") Integer page,
                                              @RequestParam(defaultValue = "10") Integer pageSize) {
        List<QaLogDTO> qaLogs = ragChatService.getQaHistory(page, pageSize);
        return Result.success(qaLogs);
    }
}
