package com.expmanagment.repo;

import com.expmanagment.entity.SignEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SignRepo extends JpaRepository<SignEntity, Integer> { }
