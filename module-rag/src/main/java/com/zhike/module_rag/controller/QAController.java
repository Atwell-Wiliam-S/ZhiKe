package com.zhike.module_rag.controller;

import com.zhike.common.util.Result;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/qa")
public class QAController {

    @PostMapping("/ask")
    public Result<?> askQuestion(@RequestBody QuestionRequest request) {
        return Result.success("回答内容");
    }

    @GetMapping("/history")
    public Result<?> getQaHistory() {
        return Result.success("问答历史");
    }

    public static class QuestionRequest {
        private String question;
        private Long videoId;
        private Double currentTime;

        public String getQuestion() {
            return question;
        }

        public void setQuestion(String question) {
            this.question = question;
        }

        public Long getVideoId() {
            return videoId;
        }

        public void setVideoId(Long videoId) {
            this.videoId = videoId;
        }

        public Double getCurrentTime() {
            return currentTime;
        }

        public void setCurrentTime(Double currentTime) {
            this.currentTime = currentTime;
        }
    }

}