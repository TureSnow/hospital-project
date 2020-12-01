package com.example.hospital.service;

import com.example.hospital.model.NaSheet;
import com.example.hospital.model.Patient;
import com.example.hospital.model.User;

import java.util.List;

public interface DoctorService {
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

    /**
     *
     * @param isHealth :是否满足出院条件
     * @return
     */
    public List<Patient> getHealthPatient(Boolean isHealth);

    public List<Patient> getNotMatchPatient();

    public Patient getPatientStateById(int patientId);

    public User getHeadNurse();

    public List<User> getWardNurses();

    public List<Patient> getPatientByWardNurseId(int wardNurseId);

    public String updatePatientIllnessLevel(int patientId, String level);

    public String updatePatientLifeStatus(int patientId, String level);

    public String addNaSheet(int patientId, NaSheet sheet);

    public String discharge(int patientId);

}
