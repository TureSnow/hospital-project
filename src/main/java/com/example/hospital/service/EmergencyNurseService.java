package com.example.hospital.service;

import com.example.hospital.model.Patient;

import java.util.List;

public interface EmergencyNurseService {
    String addPatient(String name,String illnessLevel);
    List<Patient> getAllPatient();
    List<Patient> getPateint(int area,int illnessLevel,int lifeState);
}
