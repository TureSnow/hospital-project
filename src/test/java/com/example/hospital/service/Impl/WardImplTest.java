package com.example.hospital.service.Impl;

import com.example.hospital.exception.LoginException;
import com.example.hospital.service.LoginService;
import com.example.hospital.service.WardNurseService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class WardImplTest {
    @Autowired
    private LoginService loginService;
    @Autowired
    private WardNurseService wardNurseService;
    @Test
    void addDailyStatus() throws LoginException {
        loginService.login("wnurse1","wnurse");
        wardNurseService.addDailyStatus(17,new Date(),38,1,1,"fever and cough");
    }
}