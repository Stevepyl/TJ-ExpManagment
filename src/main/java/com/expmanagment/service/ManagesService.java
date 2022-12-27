package com.expmanagment.service;


import com.alibaba.fastjson.JSONObject;
import com.expmanagment.entity.CourseEntity;
import com.expmanagment.entity.ManagesEntity;
import com.expmanagment.entity.UserEntity;
import com.expmanagment.exception.AlreadyExistException;
import com.expmanagment.exception.MyNotFoundException;
import com.expmanagment.exception.RoleNotMatchException;
import com.expmanagment.repo.ManagesRepo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class ManagesService {
    private final ManagesRepo managesRepo;

    public List<ManagesEntity> getAllByTeacherId(Integer teacherId) {
        Optional<List<ManagesEntity>> managesOptional = managesRepo.findAllByTeacherId(teacherId);
        if(managesOptional.isEmpty()) {
            throw new MyNotFoundException("该教师无管理课程");
        } else {
            return managesOptional.get();
        }
    }

    public List<ManagesEntity> getAllByCourseId(Integer courseId) {
        Optional<List<ManagesEntity>> managesOptional = managesRepo.findAllByCourseId(courseId);
        if (managesOptional.isEmpty()) {
            throw new MyNotFoundException("该课程无管理教师");
        } else {
            return managesOptional.get();
        }
    }

    public List<CourseEntity> getAllByTeacherIdInDetail(Integer teacherId) {
        Optional<List<CourseEntity>> coursesOptional = managesRepo.findAllByTeacherIdInDetail(teacherId);
        if (coursesOptional.isEmpty()) {
            throw new MyNotFoundException("该教师无管理课程");
        } else {
            return coursesOptional.get();
        }
    }

    public List<UserEntity> getAllByCourseIdInDetail(Integer courseId) {
        Optional<List<UserEntity>> teachersOptional = managesRepo.findAllByCourseIdInDetail(courseId);
        if (teachersOptional.isEmpty()) {
            //参照外码约束，以下情况一般来说不会发生
            throw new MyNotFoundException("该课程无管理教师");
        } else {
            return teachersOptional.get();
        }
    }

    public JSONObject checkExist(int uploadID, int courseID) {
        Optional<ManagesEntity> managesTemp = managesRepo.findById(uploadID,courseID);
        if (managesTemp.isEmpty()) {
            throw new MyNotFoundException("教师 " + uploadID + " 未授课课程 " + courseID);
        } else {
            JSONObject json = new JSONObject();
            json.put("code", 200);
            json.put("message", "教师授课关系存在");
            json.put("boolean",true);
            return json;
        }
    }

    public JSONObject addTeacherManages(Integer teacherId, Integer courseId) {
        Optional<ManagesEntity> managesTemp = managesRepo.findById(teacherId,courseId);
        if (managesTemp.isPresent()) {
            throw new AlreadyExistException("该教师已经授课该课程");
        }
        else
        {
            Optional<UserEntity> userTemp = managesRepo.checkTeacherId(teacherId);
            if(userTemp.isEmpty())
            {
                throw new RoleNotMatchException("该人不是教师/助教");
            }
            else
            {
                ManagesEntity managesEntity = new ManagesEntity();
                managesEntity.setTeacherId(teacherId);managesEntity.setCourseId(courseId);
                managesRepo.save(managesEntity);
                log.info("新增课程关系 => 课程ID: " + courseId + " 教师/助教ID: " + teacherId);
                JSONObject json = new JSONObject();
                json.put("code", 200);
                json.put("message", "课程关系添加成功");
                return json;
            }

        }
    }

    public JSONObject deleteTeacherManages(Integer teacherId, Integer courseId) {
        Optional<ManagesEntity> managesTemp = managesRepo.findById(teacherId,courseId);
        if (managesTemp.isEmpty()) {
            throw new MyNotFoundException("该教师未参与该课程的授课");
        }
        else
        {
            Optional<UserEntity> userTemp = managesRepo.checkTeacherId(teacherId);
            if(userTemp.isEmpty())
            {
                throw new RoleNotMatchException("该人不是教师/助教");
            }
            else
            {
                ManagesEntity managesEntity = new ManagesEntity();
                managesEntity.setTeacherId(teacherId);managesEntity.setCourseId(courseId);
                managesRepo.delete(managesEntity);
                log.info("删除课程关系 => 课程ID: " + courseId + " 教师/助教ID: " + teacherId);
                JSONObject json = new JSONObject();
                json.put("code", 200);
                json.put("message", "课程关系删除成功");
                return json;
            }
        }

    }

    public List<ManagesEntity> showAllIdByCourseId(Integer courseId) {
        List<ManagesEntity> list = managesRepo.findAllById(courseId);
        if(list.isEmpty())
        {
            throw new MyNotFoundException("该课程无教师任课");
        }
        else
        {
            return list;
        }
    }

    public List<CourseEntity> getCourseByManagerId(Integer managerId) {
        List<CourseEntity> list = managesRepo.getCourseByManagerId(managerId);
        if(list.isEmpty())
        {
            throw new MyNotFoundException("该教师无主管课程");
        }
        else
        {
            return list;
        }
    }

    public List<CourseEntity> getTeachingCourse(Integer teacherId) {
        List<CourseEntity> list = managesRepo.getTeachingCourse(teacherId);
        if(list.isEmpty())
        {
            throw new MyNotFoundException("该教师不存在除存在主管课程以外的课程");
        }
        else
        {
            return list;
        }
    }
}
