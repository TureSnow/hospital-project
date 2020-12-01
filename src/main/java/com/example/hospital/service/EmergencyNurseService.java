package com.example.hospital.service;

import com.example.hospital.model.Patient;

import java.util.List;

public interface EmergencyNurseService {
    public String addPatient(Patient patient);
    public List<Patient> getAllPatient();
    /**
     * @param isHealth :是否满足出院条件
     * @return
     */
    public List<Patient> getHealthPatient(Boolean isHealth);

    public List<Patient> getPatientByArea(int area);

    public List<Patient> getIsoPatient();

    public Patient getPatientLifeStateById(int patientId);

    public Patient getPatientIllnessLevelById(int patientId);
}
