package com.zhike.module_video.service;

import com.zhike.module_video.model.Video;
import com.zhike.module_video.model.KnowledgeFragment;

import java.util.List;
import java.util.Map;

/**
 * 视频服务接口
 * 提供视频相关的业务逻辑操作
 */
public interface VideoService {

    /**
     * 获取OSS上传签名
     * @param fileName 文件名
     * @param fileSize 文件大小
     * @param courseId 课程ID
     * @return 签名信息
     */
    Map<String, String> getUploadSignature(String fileName, long fileSize, Long courseId);

    /**
     * 视频上传完成处理
     * @param courseId 课程ID
     * @param title 视频标题
     * @param ossUrl OSS地址
     * @param duration 视频时长
     * @return 视频对象
     */
    Video uploadComplete(Long courseId, String title, String ossUrl, Integer duration);

    /**
     * 触发视频解析
     * @param videoId 视频ID
     */
    void triggerParse(Long videoId);

    /**
     * 获取视频解析状态
     * @param videoId 视频ID
     * @return 解析状态信息
     */
    Map<String, Object> getParseStatus(Long videoId);

    /**
     * 获取视频播放签名URL
     * @param videoId 视频ID
     * @return 签名URL信息
     */
    Map<String, Object> getSignUrl(Long videoId);

    /**
     * 获取教师的视频列表
     * @param courseId 课程ID
     * @param page 页码
     * @param pageSize 每页条数
     * @return 视频列表
     */
    List<Video> getTeacherVideos(Long courseId, Integer page, Integer pageSize);

    /**
     * 获取知识片段列表
     * @param videoId 视频ID
     * @return 知识片段列表
     */
    List<KnowledgeFragment> getFragments(Long videoId);

    /**
     * 更新知识片段
     * @param fragmentId 片段ID
     * @param contentText 融合文本
     * @param summary 摘要
     * @param keywords 关键词
     */
    void updateFragment(Long fragmentId, String contentText, String summary, String keywords);

    /**
     * 删除知识片段
     * @param fragmentId 片段ID
     */
    void deleteFragment(Long fragmentId);
}
