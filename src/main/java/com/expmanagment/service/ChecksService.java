package com.expmanagment.service;


import com.alibaba.fastjson.JSONObject;
import com.expmanagment.entity.ChecksEntity;
import com.expmanagment.entity.FinishesEntity;
import com.expmanagment.exception.StudentNotFoundException;
import com.expmanagment.exception.TaskNotFoundException;
import com.expmanagment.exception.TeacherNotFoundException;
import com.expmanagment.repo.ChecksRepo;
import com.expmanagment.repo.FinishesRepo;
import com.expmanagment.repo.TaskRepo;
import com.expmanagment.repo.UserRepo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class ChecksService {
    private final UserRepo userRepo;
    private final ChecksRepo checksRepo;
    private final TaskRepo taskRepo;
    private final FinishesRepo finishesRepo;

    public JSONObject add(ChecksEntity checks) {
        if (!userRepo.existsById(checks.getTeacherId())) {
            throw new TeacherNotFoundException("该教师不存在");
        } else if (!userRepo.existsById(checks.getStudentId())) {
            throw new StudentNotFoundException("该学生不存在");
        } else if (!taskRepo.existsById(checks.getTaskId())) {
            throw new TaskNotFoundException("该实验项目不存在");
        }
        Optional<FinishesEntity> finishesOptional = finishesRepo.findById(checks.getStudentId(), checks.getTaskId());
        if (finishesOptional.isPresent()) {
            FinishesEntity finishes = finishesOptional.get();
            finishes.setScore(checks.getScore());
            finishesRepo.save(finishes);
        } else {
            FinishesEntity finishes = new FinishesEntity();
            finishes.setStudentId(checks.getStudentId());
            finishes.setTaskId(checks.getTaskId());
            finishes.setFinished(0);
            finishes.setScore(checks.getScore());
            finishesRepo.save(finishes);

            log.info("新增作业提交记录：" +
                    "学生 " + checks.getStudentId() +
                    " 没有提交，但是教师" + checks.getTeacherId() +
                    "批改了。");
        }
        checks.setCheckTime(Timestamp.valueOf(LocalDateTime.now()));
        checksRepo.save(checks);
        log.info("新增批改记录：" +
                "教师 " + checks.getTeacherId() + " 在 " +
                checks.getCheckTime() + " 时间批改了" +
                "学生 " + checks.getStudentId() +
                " 的实验项目 " + checks.getTaskId() +
                " 的成绩：" + checks.getScore());
        JSONObject json = new JSONObject();
        json.put("code", 200);
        json.put("message", "新增批改记录成功");
        return json;
    }
}
