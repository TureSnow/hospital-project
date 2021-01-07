package com.example.hospital.service;

import com.example.hospital.model.Patient;

import java.util.List;

public interface EmergencyNurseService {
    /**
     * 添加病人
     * @param name
     * @param illnessLevel
     * @return
     */
    String addPatient(String name,String illnessLevel);
    List<Patient> getAllPatient();
    List<Patient> getPatient(int area, int illnessLevel, int lifeState);
}
