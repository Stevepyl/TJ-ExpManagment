package com.expmanagment.repo;

import com.expmanagment.entity.VerificationCodeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VerificationCodeRepo extends JpaRepository<VerificationCodeEntity, Integer> {
}
