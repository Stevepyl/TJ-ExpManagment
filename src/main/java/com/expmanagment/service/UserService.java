package com.expmanagment.service;


import com.expmanagment.repo.UserRepo;
import com.expmanagment.repo.VerificationCodeRepo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class UserService {
    private final UserRepo userRepo;
    private final VerificationCodeRepo verificationCodeRepo;


}
