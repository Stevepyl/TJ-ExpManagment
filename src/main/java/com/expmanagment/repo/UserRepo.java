package com.expmanagment.repo;

import com.expmanagment.entity.UserEntity;
import org.apache.catalina.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepo extends JpaRepository<UserEntity, Integer> {
    @Query(value = "select s from UserEntity s where s.email = ?1")
    public Optional<UserEntity> findAllByEmail(String email);

    @Query(value = "select u from UserEntity u where u.identity = 1")
    public List<UserEntity> findAllStudents();

    @Query(value = "select u from UserEntity u where u.identity = 2 or u.identity = 3")
    public List<UserEntity> findAllTeachers();

    @Query(value = "select u from UserEntity u where u.identity = 4")
    public List<UserEntity> findAllAdministrators();
}
