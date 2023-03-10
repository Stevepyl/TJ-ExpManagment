package com.expmanagment.repo;

import com.expmanagment.entity.CourseEntity;
import com.expmanagment.entity.ManagesEntity;
import com.expmanagment.entity.ManagesEntityPK;
import com.expmanagment.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ManagesRepo extends JpaRepository<ManagesEntity, ManagesEntityPK> {

    @Query(value = "select m from ManagesEntity m where m.teacherId = ?1 and m.courseId = ?2")
    public Optional<ManagesEntity> findById(Integer uploadID, Integer courseID);

    @Query(value = "select s from UserEntity s where s.id=?1 and (s.identity = 3 or s.identity = 4 or s.identity = 2 )")
    Optional<UserEntity> checkTeacherId(Integer teacherId);

    @Query(value = "select s from ManagesEntity s where s.courseId = ?1 ")
    List<ManagesEntity> findAllById(Integer courseId);
    /**
     * 以下两个接口只返回Manages实体
     */
    @Query(value = "select m from ManagesEntity m where m.teacherId = ?1")
    public Optional<List<ManagesEntity>> findAllByTeacherId(Integer teacherId);

    @Query(value = "select m from ManagesEntity m where m.courseId = ?1")
    public Optional<List<ManagesEntity>> findAllByCourseId(Integer courseId);

    /**
     * 以下两个接口返回查询对象的实体
     */
    @Query(value = "select c from ManagesEntity m, CourseEntity c where m.courseId = c.id and m.teacherId = ?1")
    public Optional<List<CourseEntity>> findAllByTeacherIdInDetail(Integer teacherId);

    @Query(value = "select u from ManagesEntity m, UserEntity u where m.teacherId = u.id and m.courseId = ?1")
    public Optional<List<UserEntity>> findAllByCourseIdInDetail(Integer courseId);

    @Query(value = "select u from CourseEntity u where u.manager = ?1")
    public List<CourseEntity> getCourseByManagerId(Integer managerId);

    @Query(value = " select u from ManagesEntity m, CourseEntity u where m.teacherId = ?1 " +
            "and m.courseId = u.id and u.manager <> ?1")
    public List<CourseEntity> getTeachingCourse(Integer teacherId);

}
