package com.expmanagment.service;


import com.expmanagment.repo.ChecksRepo;
import com.expmanagment.repo.UserRepo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class ChecksService {
    private final UserRepo userRepo;
    private final ChecksRepo checksRepo;

}
