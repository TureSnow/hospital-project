package com.example.hospital.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class EmergencyNurseServiceTest {
    @Autowired
    private EmergencyNurseService emergencyNurseService;
    @Test
    void addPatient() {
//        System.out.println("111111111111111111111111111111111111111111111");
//        System.out.println(emergencyNurseService.addPatient("ptt1","1"));
        emergencyNurseService.addPatient("ptt2","1");
        emergencyNurseService.addPatient("ptt3","1");
        emergencyNurseService.addPatient("ptt4","1");
        emergencyNurseService.addPatient("ptt5","1");
        emergencyNurseService.addPatient("ptt6","1");

        emergencyNurseService.addPatient("ptt7","2");
        emergencyNurseService.addPatient("ptt8","2");
        emergencyNurseService.addPatient("ptt9","2");

        emergencyNurseService.addPatient("ptt10","3");
        emergencyNurseService.addPatient("ptt11","3");

        emergencyNurseService.addPatient("ptt12","3");
    }

    @Test
    void getAllPatient() {
    }

    @Test
    void getPateint() {
    }
}