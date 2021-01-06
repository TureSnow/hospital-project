package com.example.hospital.service.Impl;

import com.example.hospital.exception.LoginException;
import com.example.hospital.model.NaSheet;
import com.example.hospital.service.DoctorService;
import com.example.hospital.service.LoginService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class DoctorServiceImplTest {
    @Autowired
    private LoginService loginService;
    @Autowired
    private DoctorService doctorService;
    @Test
    void getArea() {
    }

    @Test
    void getAllPatient() {
    }

    @Test
    void getPatient() {
    }

    @Test
    void getPatientCanDischarge() {
    }

    @Test
    void getNotMatchPatient() {
    }

    @Test
    void transferOtherArea() {
    }

    @Test
    void getPatientStateById() {
    }

    @Test
    void getHeadNurse() {
    }

    @Test
    void getWardNurses() {
    }

    @Test
    void getPatientByWardNurseId() {
    }

    @Test
    void updatePatientIllnessLevel() {
    }

    @Test
    void updatePatientLifeStatus() {
    }

    @Test
    void addNaSheet() throws LoginException {
        int patientId = 16;
        Date date=new Date();
        String na="0";
        String ill = "1";
    if (loginService==null){
        System.out.println("login null");
    }
        loginService.login("doctor1","doctor");
        doctorService.addNaSheet(patientId,date,na,ill);
    }

    @Test
    void discharge() {
    }
}