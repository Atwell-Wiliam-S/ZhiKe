package com.zhike.module_student.controller;

import com.zhike.Application;
import com.zhike.module_student.dto.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StudentControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    // 测试课程列表接口
    @Test
    public void testGetCourses() {
        ResponseEntity<List> response = restTemplate.getForEntity("/student/courses", List.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isEmpty());
    }

    // 测试课程详情接口
    @Test
    public void testGetCourseDetail() {
        ResponseEntity<CourseDetailDTO> response = restTemplate.getForEntity("/student/courses/1", CourseDetailDTO.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("高等数学 A", response.getBody().getTitle());
        assertNotNull(response.getBody().getVideos());
        assertFalse(response.getBody().getVideos().isEmpty());
    }

    // 测试视频播放信息接口
    @Test
    public void testGetVideoPlayInfo() {
        ResponseEntity<PlayInfoDTO> response = restTemplate.getForEntity("/student/videos/1/play-info", PlayInfoDTO.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getVideoId());
        assertNotNull(response.getBody().getSignUrl());
        assertNotNull(response.getBody().getFragments());
        assertFalse(response.getBody().getFragments().isEmpty());
    }

    // 测试心跳上报接口
    @Test
    public void testHeartbeat() {
        HeartbeatRequest request = new HeartbeatRequest();
        request.setVideoId(1);
        request.setCurrentTime(300.5);
        request.setStatus("PLAYING");
        
        ResponseEntity<Void> response = restTemplate.postForEntity("/student/records/heartbeat", request, Void.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    // 测试进度保存接口
    @Test
    public void testSaveProgress() {
        ProgressRequest request = new ProgressRequest();
        request.setVideoId(1);
        request.setCurrentTime(450.0);
        request.setDuration(600);
        
        ResponseEntity<Void> response = restTemplate.postForEntity("/student/records/progress", request, Void.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    // 测试添加笔记接口
    @Test
    public void testAddNote() {
        NoteRequest request = new NoteRequest();
        request.setVideoId(1);
        request.setTimestamp(120.5);
        request.setContent("这是一个测试笔记");
        
        ResponseEntity<Integer> response = restTemplate.postForEntity("/student/notes", request, Integer.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() > 0);
    }

    // 测试获取笔记列表接口
    @Test
    public void testGetNotes() {
        // 先添加一个笔记
        NoteRequest request = new NoteRequest();
        request.setVideoId(1);
        request.setTimestamp(120.5);
        request.setContent("这是一个测试笔记");
        restTemplate.postForEntity("/student/notes", request, Integer.class);
        
        // 然后获取笔记列表
        ResponseEntity<List> response = restTemplate.getForEntity("/student/notes?videoId=1", List.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isEmpty());
    }
}