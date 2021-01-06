package com.example.hospital.service;

import com.example.hospital.model.Bed;
import com.example.hospital.model.Patient;
import com.example.hospital.model.User;

import java.util.List;

public interface HeadNurseService {
    List<Patient> getAllPatient();
    /**
     * 筛选病人
     * @param lifeState  1:health; 2:treating; 3:dead; 4:all
     * @param isMatchWard 1:match; 2:dismatch; 3:all ok
     * @param IllnessLevel 1:mild; 2:severe; 3:critical; 4:all ok
     * @return 满足筛选条件的病人
     */
    List<Patient> getPatient(int lifeState, int IllnessLevel, int isMatchWard);

    List<Patient> getNotMatchPatient();

    List<User> getWardNurses();

    List<Patient> getPatientByWardNurseId(int wardNurseId);

    /**
     * 将已有的user添加到治疗区域
     * @param wardNurseId
     * @return msg
     */
    public String addWardNurse(int wardNurseId);

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
