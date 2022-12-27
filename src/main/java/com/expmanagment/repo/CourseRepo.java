package com.expmanagment.repo;

import com.expmanagment.entity.CourseEntity;
import io.swagger.models.auth.In;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

public interface CourseRepo extends JpaRepository<CourseEntity, Integer> {
    @Query(value = "select c from CourseEntity c where c.name = ?1")
    public Optional<List<CourseEntity>> findAllByName(String name);

    @Query(value = "select c from CourseEntity c where c.year = ?1")
    public Optional<List<CourseEntity>> findAllByYear(Integer year);

    @Query(value = "select c from CourseEntity c where c.year = ?1 and c.semester = ?2")
    public Optional<List<CourseEntity>> findAllByYearAndSemester(Integer year, Integer semester);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "update CourseEntity s set s.description = ?2,s.name=?3,s.semester=?4,s.year =?5,s.manager=?6 where " +
            "s.id = ?1")
    void update(int id, String description, String name, Integer semester, Integer year, Integer manager);

}
