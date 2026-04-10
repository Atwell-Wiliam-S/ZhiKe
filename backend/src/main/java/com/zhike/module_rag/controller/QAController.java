package com.zhike.module_rag.controller;

import com.zhike.common.util.Result;
import com.zhike.module_rag.service.RagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;

@RestController
@RequestMapping("/qa")
public class QAController {

    @Autowired
    private RagService ragService;

    @PostMapping("/chat-stream")
    public SseEmitter chatStream(Authentication authentication, @RequestBody Map<String, Object> request) {
        Long userId = Long.valueOf(authentication.getName());
        String question = (String) request.get("question");
        Long videoId = (Long) request.get("videoId");
        Double currentTime = (Double) request.get("currentTime");

        return ragService.chatStream(userId, question, videoId, currentTime);
    }

    @GetMapping("/history")
    public Result<?> getHistory(Authentication authentication, @RequestParam Integer page, @RequestParam Integer pageSize) {
        Long userId = Long.valueOf(authentication.getName());
        return Result.success(ragService.getHistory(userId, page, pageSize));
    }
}
