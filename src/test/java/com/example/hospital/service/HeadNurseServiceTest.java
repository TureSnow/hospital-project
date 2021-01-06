package com.example.hospital.service;

import com.example.hospital.exception.LoginException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class HeadNurseServiceTest {
    @Autowired
    private LoginService loginService;
    @Autowired
    private HeadNurseService headNurseService;
    @Test
    void getAllPatient() {
    }

    @Test
    void getPatient() {
    }

    @Test
    void getNotMatchPatient() {
    }

    @Test
    void getWardNurses() {
    }

    @Test
    void getPatientByWardNurseId() {
    }

    @Test
    void addWardNurse() {
        try {
            if (loginService == null){
                System.out.println("login null");
                return;
            }
//            loginService.login("hnurse1","hnurse");
//            headNurseService.addWardNurse(8);
//            headNurseService.addWardNurse(14);
//            headNurseService.addWardNurse(19);
//            loginService.login("anna","123456");
//            headNurseService.addWardNurse(15);
//            headNurseService.addWardNurse(16);
//            loginService.login("david","123456");
//            headNurseService.addWardNurse(17);
//            headNurseService.addWardNurse(18);
        } catch (LoginException e) {
            e.printStackTrace();
        }
    }

    @Test
    void deleteWardNurse() {
    }

    @Test
    void getAllBed() {
    }

    @Test
    void getPatientByBed() {
    }
}