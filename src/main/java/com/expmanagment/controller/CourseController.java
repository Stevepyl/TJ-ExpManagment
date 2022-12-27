package com.expmanagment.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.alibaba.fastjson.JSONObject;
import com.expmanagment.entity.CourseEntity;
import com.expmanagment.service.CourseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "课程管理")
@RestController
@RequestMapping("course")
@AllArgsConstructor
@CrossOrigin
public class CourseController {
    private final CourseService courseService;

    @SaCheckLogin
    @ApiOperation("按ID获取课程信息")
    @GetMapping("/get")
    public CourseEntity getById(Integer id) {
        return courseService.getById(id);
    }

    @SaCheckLogin
    @ApiOperation("按课程名获取课程信息")
    @GetMapping("/getByName")
    public List<CourseEntity> getAllByName(String name) {
        return courseService.getAllByName(name);
    }

    @SaCheckLogin
    @ApiOperation("按学年获取课程信息")
    @GetMapping("/getByYear")
    public List<CourseEntity> getAllByYear(Integer year) {
        return courseService.getAllByYear(year);
    }

    @SaCheckLogin
    @ApiOperation("按学年和学期获取课程信息")
    @GetMapping("/getByYearAndSemester")
    public List<CourseEntity> getAllByYearAndSemester(Integer year, Integer semester) {
        return courseService.getAllByYearAndSemester(year, semester);
    }

    @SaCheckLogin
    @ApiOperation("新增课程")
    @PostMapping("/add")
    public JSONObject add(@RequestBody CourseEntity course) {
        return courseService.add(course);
    }

    @SaCheckLogin
    @ApiOperation("删除课程【注意：此操作会将所有该课程的学生选课信息一并移除！】")
    @DeleteMapping("/remove")
    public JSONObject remove(Integer courseId) {
        return courseService.remove(courseId);
    }

    @SaCheckLogin
    @ApiOperation("修改课程信息")
    @PutMapping("/update")
    public JSONObject updateCourse(@RequestBody CourseEntity course) {
        return courseService.update(course);
    }

}
