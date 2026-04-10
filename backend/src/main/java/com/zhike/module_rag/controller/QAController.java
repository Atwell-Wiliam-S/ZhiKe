package com.zhike.module_rag.controller;

import com.zhike.common.util.Result;
import com.zhike.module_rag.service.RagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;

/**
 * RAG智能问答控制器
 * 处理智能问答、问答历史查询等操作
 */
@RestController
@RequestMapping("/qa")
public class QAController {

    @Autowired
    private RagService ragService;

    /**
     * 流式问答接口
     * @param authentication 认证信息
     * @param request 问答请求参数
     * @return SSE事件流
     */
    @PostMapping("/chat-stream")
    public SseEmitter chatStream(Authentication authentication, @RequestBody Map<String, Object> request) {
        Long userId = Long.valueOf(authentication.getName());
        String question = (String) request.get("question");
        Long videoId = (Long) request.get("videoId");
        Double currentTime = (Double) request.get("currentTime");

        return ragService.chatStream(userId, question, videoId, currentTime);
    }

    /**
     * 获取问答历史
     * @param authentication 认证信息
     * @param page 页码
     * @param pageSize 每页条数
     * @return 问答历史列表
     */
    @GetMapping("/history")
    public Result<?> getHistory(Authentication authentication, @RequestParam Integer page, @RequestParam Integer pageSize) {
        Long userId = Long.valueOf(authentication.getName());
        return Result.success(ragService.getHistory(userId, page, pageSize));
    }
}
