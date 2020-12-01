package com.example.hospital.service;

import com.example.hospital.model.Bed;
import com.example.hospital.model.Patient;
import com.example.hospital.model.User;

import java.util.List;

public interface HeadNurseService {
    public List<Patient> getAllPatient();

    /**
     * 筛选
     * @param isHealth
     * @param isMatchWard
     * @param lifeStatus
     * @param IllnessLevel
     * @return
     */
    public List<Patient> getPatient(Boolean isHealth,Boolean isMatchWard,String lifeStatus, String IllnessLevel);

    public List<Patient> getNotMatchPatient();

    public List<Patient> getPatientLifeStateById(int patientId);

    public List<User> getWardNurses();

    public List<Patient> getPatientByWardNurseId(int wardNurseId);

    public String addWardNurse(User wardNurse);

    public String deleteWardNurse(int wardNurseId);

    public List<Bed> getAllBed();

    public Patient getPatientByBed(int Bed);

}
