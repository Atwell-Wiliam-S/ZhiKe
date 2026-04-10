package com.zhike.module_video.service;

import com.zhike.module_video.model.Video;
import com.zhike.module_video.model.KnowledgeFragment;

import java.util.List;
import java.util.Map;

public interface VideoService {

    Map<String, String> getUploadSignature(String fileName, long fileSize, Long courseId);

    Video uploadComplete(Long courseId, String title, String ossUrl, Integer duration);

    void triggerParse(Long videoId);

    Map<String, Object> getParseStatus(Long videoId);

    Map<String, Object> getSignUrl(Long videoId);

    List<Video> getTeacherVideos(Long courseId, Integer page, Integer pageSize);

    List<KnowledgeFragment> getFragments(Long videoId);

    void updateFragment(Long fragmentId, String contentText, String summary, String keywords);

    void deleteFragment(Long fragmentId);
}
