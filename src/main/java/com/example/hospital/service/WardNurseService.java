package com.example.hospital.service;

import com.example.hospital.model.DailyStatus;
import com.example.hospital.model.Patient;

import java.util.List;

public interface WardNurseService {
    public String addDailyStatus(int patientId, DailyStatus dailyStatus);

    /**
     *  根据条件筛选
     * @param canDischarge
     * @param lifeState
     * @return
     */
    public List<Patient> getPatient(boolean canDischarge,String lifeState);

    public List<Patient> getAllPatient();
}
