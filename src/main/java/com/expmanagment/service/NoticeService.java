package com.expmanagment.service;

import com.alibaba.fastjson.JSONObject;
import com.expmanagment.entity.NoticeEntity;
import com.expmanagment.exception.AlreadyExistException;
import com.expmanagment.exception.MyNotFoundException;
import com.expmanagment.repo.NoticeRepo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


@Service
@AllArgsConstructor
@Slf4j
public class NoticeService {

    private final NoticeRepo noticeRepo;

    public JSONObject addNotice(NoticeEntity notice) {
        if(noticeRepo.findByTopic(notice.getTopic()).isPresent())
        {
            throw new AlreadyExistException("同名公告已存在");
        }
        else {
            notice.setUpdatedTime(Date.valueOf(LocalDate.now()));
            noticeRepo.save(notice);
            JSONObject json = new JSONObject();
            json.put("code", 200);
            json.put("message", "添加成功");
            return json;
        }
    }

    public List<NoticeEntity> showNoticeByTimeDesc() {
        if (noticeRepo.findAll().isEmpty())
        {
            throw new MyNotFoundException("无任何公告");
        }
        return noticeRepo.showNoticeByTime();
    }

    public List<NoticeEntity> getAllForStudent() {
        List<NoticeEntity> allForStudent = noticeRepo.findAllForStudent();
        if (allForStudent.isEmpty()) {
            throw new MyNotFoundException("无任何公告");
        } else {
            return allForStudent;
        }
    }

    public List<NoticeEntity> getAllForTeachers() {
        List<NoticeEntity> allForTeachers = noticeRepo.findAllForTeachers();
        if (allForTeachers.isEmpty()) {
            throw new MyNotFoundException("无任何公告");
        } else {
            return allForTeachers;
        }
    }

    public JSONObject deleteNotice(int id) {
        if(noticeRepo.findById(id).isEmpty())
        {
            throw new MyNotFoundException("该公告不存在");
        }
        else
        {
            noticeRepo.deleteById(id);
            JSONObject json = new JSONObject();
            json.put("code", 200);
            json.put("message", "公告删除成功");
            return json;
        }
    }

    public JSONObject updateNotice(NoticeEntity notice) {
        Optional<NoticeEntity> noticeOptional = noticeRepo.findById(notice.getId());
        if(noticeOptional.isEmpty())
        {
            throw new MyNotFoundException("该公告不存在");
        }
        else
        {
            if (notice.getTopic().equals("")) {
                log.info("用户未更新公告标题");
            } else {
                noticeOptional.get().setTopic(notice.getTopic());
            }
            if (notice.getContent().equals("")) {
                log.info("用户未更新公告内容");
            } else {
                noticeOptional.get().setContent(notice.getContent());
            }
            if (notice.getType() == null) {
                log.info("用户未更新公告类型");
            } else {
                noticeOptional.get().setType(notice.getType());
            }
            // ↓更新公告更新时间由后端完成
            noticeOptional.get().setUpdatedTime(Date.valueOf(LocalDate.now()));
            noticeRepo.save(noticeOptional.get());
            JSONObject json = new JSONObject();
            json.put("code", 200);
            json.put("message", "公告修改成功");
            return json;
        }
    }
}