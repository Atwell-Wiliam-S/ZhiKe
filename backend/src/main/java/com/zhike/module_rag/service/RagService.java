package com.zhike.module_rag.service;

import com.zhike.module_rag.model.QaLog;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.Map;

/**
 * RAG智能问答服务接口
 * 提供基于检索增强生成的智能问答功能
 */
public interface RagService {

    /**
     * 流式问答接口
     * 使用SSE（Server-Sent Events）实现流式响应，实时返回AI生成的回答
     * 
     * @param userId 用户ID
     * @param question 用户问题
     * @param videoId 视频ID，用于检索相关知识片段
     * @param currentTime 当前观看时间点，用于定位相关知识片段
     * @return SseEmitter对象，用于发送流式响应
     */
    SseEmitter chatStream(Long userId, String question, Long videoId, Double currentTime);

    /**
     * 获取用户问答历史记录
     * 
     * @param userId 用户ID
     * @param page 页码
     * @param pageSize 每页数量
     * @return 包含问答历史记录和分页信息的Map
     */
    Map<String, Object> getHistory(Long userId, Integer page, Integer pageSize);
}
