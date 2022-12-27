package com.expmanagment.repo;

import com.expmanagment.entity.TakesEntity;
import com.expmanagment.entity.TakesEntityPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface TakesRepo extends JpaRepository<TakesEntity, TakesEntityPK> {
    @Query(value = "select t from TakesEntity t where t.studentId = ?1 and t.courseId = ?2")
    public Optional<TakesEntity> findById(Integer studentId, Integer courseId);

    @Query(value = "select t from TakesEntity t where t.studentId = ?1")
    public Optional<List<TakesEntity>> findAllByStudentId(Integer studentId);

    @Query(value = "select t from TakesEntity t where t.courseId = ?1")
    public Optional<List<TakesEntity>> findAllByCourseId(Integer courseId);

    @Transactional
    @Modifying
    @Query(value = "delete from takes where student_id = ?1 and course_id = ?2", nativeQuery = true)
    public void remove(Integer studentId, Integer courseId);
}
