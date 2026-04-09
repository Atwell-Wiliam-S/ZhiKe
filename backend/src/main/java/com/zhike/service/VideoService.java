package com.zhike.service;

import com.zhike.entity.Video;

import java.util.List;

public interface VideoService {
    List<Video> getAllVideos();
    Video getVideoById(Integer id);
    List<Video> getVideosByCourseId(Integer courseId);
    List<Video> getVideosByParseStatus(Integer status);
    Integer createVideo(Video video);
    Integer updateVideo(Video video);
    Integer deleteVideo(Integer id);
    void parseVideo(Integer videoId);
}
