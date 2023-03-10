package com.expmanagment.controller;


import cn.dev33.satoken.annotation.SaCheckLogin;
import com.alibaba.fastjson.JSONObject;
import com.expmanagment.entity.CourseEntity;
import com.expmanagment.entity.ManagesEntity;
import com.expmanagment.entity.UserEntity;
import com.expmanagment.service.ManagesService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "教师授课信息管理")
@RestController
@RequestMapping("manages")
@AllArgsConstructor
@CrossOrigin
public class ManagesController {
    private final ManagesService managesService;

    @SaCheckLogin
    @ApiOperation("增加老师/助教执教课程关系")
    @RequestMapping("/add")
    public JSONObject addTeacherManages(Integer teacherId, Integer courseId) {
        return managesService.addTeacherManages(teacherId, courseId);
    }

    @SaCheckLogin
    @ApiOperation("删除老师/助教执教课程关系")
    @DeleteMapping("/delete")
    public JSONObject deleteTeacherManages(Integer teacherId, Integer courseId)
    {
        return managesService.deleteTeacherManages(teacherId,courseId);
    }

    @SaCheckLogin
    @ApiOperation("按教师ID获取其任课的所有课程")
    @GetMapping("/get/teacher")
    public List<ManagesEntity> getAllByTeacherId(Integer teacherId) {
        return managesService.getAllByTeacherId(teacherId);
    }

    @SaCheckLogin
    @ApiOperation("按课程ID获取所有任课教师")
    @GetMapping("/get/course")
    public List<ManagesEntity> getAllByCourseId(Integer courseId) {
        return managesService.getAllByCourseId(courseId);
    }

    @SaCheckLogin
    @ApiOperation("按教师ID获取其管理所有课程详细信息")
    @GetMapping("/get/teacher/detail")
    public List<CourseEntity> getAllByTeacherIdInDetail(Integer teacherId) {
        return managesService.getAllByTeacherIdInDetail(teacherId);
    }

    @SaCheckLogin
    @ApiOperation("按课程ID获取所有任课教师详细信息")
    @GetMapping("/get/course/detail")
    public List<UserEntity> getAllByCourseIdInDetail(Integer courseId) {
        return managesService.getAllByCourseIdInDetail(courseId);
    }

    @SaCheckLogin
    @ApiOperation("按教师ID获得其为责任教师的课程")
    @GetMapping("/get/course/managerId")
    public List<CourseEntity> getCourseByManagerId(Integer managerId)
    {
        return managesService.getCourseByManagerId(managerId);
    }

    @SaCheckLogin
    @ApiOperation("按教师ID获得其任课的课程（去除为责任教师的）")
    @GetMapping("/get/teachingCourse")
    public List<CourseEntity> getTeachingCourse(Integer teacherId)
    {
        return managesService.getTeachingCourse(teacherId);
    }
}
