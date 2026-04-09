package com.zhike.service;

import com.zhike.entity.Course;

import java.util.List;

public interface CourseService {
    List<Course> getAllCourses();
    Course getCourseById(Integer id);
    List<Course> getCoursesByTeacherId(Integer teacherId);
    List<Course> getPublishedCourses();
    Integer createCourse(Course course);
    Integer updateCourse(Course course);
    Integer deleteCourse(Integer id);
}
