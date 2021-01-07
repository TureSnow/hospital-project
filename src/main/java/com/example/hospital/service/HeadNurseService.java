package com.example.hospital.service;

import com.example.hospital.model.Bed;
import com.example.hospital.model.Patient;
import com.example.hospital.model.User;

import java.util.List;

public interface HeadNurseService {
    List<Patient> getAllPatient();

    List<Patient> getNotMatchPatient();

    List<User> getWardNurses();

    List<Patient> getPatientByWardNurseId(int wardNurseId);

    /**
     * 将已有的user添加到治疗区域
     * @param wardNurseName
     * @return msg
     */
    String addWardNurse(String wardNurseName);

    /**
     * ward nurse从治疗区域移除
     * @param wardNurseId
     * @return msg
     */
    String deleteWardNurse(int wardNurseId);

    /**
     *all beds in this treatment
     * @return
     */
    List<Bed> getAllBed();

    /**
     *get patient in specified bed
     * @param BedId
     * @return
     */
    Patient getPatientByBed(int BedId);

}
