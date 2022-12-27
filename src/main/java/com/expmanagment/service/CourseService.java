package com.expmanagment.service;


import com.alibaba.fastjson.JSONObject;
import com.expmanagment.entity.CourseEntity;
import com.expmanagment.entity.ManagesEntity;
import com.expmanagment.exception.AlreadyExistException;
import com.expmanagment.exception.CourseNotFoundException;
import com.expmanagment.exception.MyNotFoundException;
import com.expmanagment.repo.CourseRepo;
import com.expmanagment.repo.ManagesRepo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class CourseService {
    private final CourseRepo courseRepo;
    private final ManagesRepo managesRepo;

    public CourseEntity getById(Integer id) {
        Optional<CourseEntity> courseOptional = courseRepo.findById(id);
        if (courseOptional.isEmpty()) {
            throw new CourseNotFoundException("ID为 " + id + " 的课程不存在！");
        } else {
            return courseOptional.get();
        }
    }

    public List<CourseEntity> getAllByName(String name) {
        Optional<List<CourseEntity>> coursesOptional = courseRepo.findAllByName(name);
        if (coursesOptional.isEmpty()) {
            throw new CourseNotFoundException("没有名称为 " + name + " 的课程！");
        } else {
            return coursesOptional.get();
        }
    }

    public List<CourseEntity> getAllByYear(Integer year) {
        Optional<List<CourseEntity>> coursesOptional = courseRepo.findAllByYear(year);
        if (coursesOptional.isEmpty()) {
            throw new MyNotFoundException("没有课程在 " + year + " 年开课！");
        } else {
            return coursesOptional.get();
        }
    }

    public List<CourseEntity> getAllByYearAndSemester(Integer year, Integer semester) {
        Optional<List<CourseEntity>> coursesOptional = courseRepo.findAllByYearAndSemester(year, semester);
        if (coursesOptional.isEmpty()) {
            throw new MyNotFoundException("没有课程在 " + year + " 年第 " + semester + " 学期开课！");
        } else {
            return coursesOptional.get();
        }
    }

    public JSONObject add(CourseEntity course) {
        Optional<CourseEntity> courseOptional = courseRepo.findById(course.getId());
        if (courseOptional.isPresent()) {
            throw new AlreadyExistException("该课程已存在");
        } else {
            courseRepo.save(course);

            log.info("新增课程 => 课程ID: " + course.getId() + " 课程名: " + course.getName());

            ManagesEntity manages = new ManagesEntity();
            manages.setCourseId(course.getId());
            manages.setTeacherId(course.getManager());
            managesRepo.save(manages);
            log.info("新增责任教师授课关系：教师 " + course.getManager() + " 课程 " + course.getId());

            JSONObject json = new JSONObject();
            json.put("code", 200);
            json.put("message", "课程添加成功");
            return json;
        }
    }

    public JSONObject remove(Integer courseId) {
        boolean courseExists = courseRepo.existsById(courseId);
        if (!courseExists) {
            throw new CourseNotFoundException("该课程不存在");
        } else {
            courseRepo.deleteById(courseId);
            log.info("删除课程: 课程ID " + courseId);
            JSONObject json = new JSONObject();
            json.put("code", 200);
            json.put("message", "删除课程成功！");
            return json;
        }
    }

    public JSONObject update(CourseEntity course) {
        boolean courseExists = courseRepo.existsById(course.getId());
        if (!courseExists) {
            throw new CourseNotFoundException("该课程不存在");
        } else {
            courseRepo.update(course.getId(),course.getDescription(),course.getName(),course.getSemester()
                    ,course.getYear(),course.getManager());
            log.info("修改课程: 课程ID " + course.getId());
            JSONObject json = new JSONObject();
            json.put("code", 200);
            json.put("message", "修改课程成功！");
            return json;
        }
    }
}
