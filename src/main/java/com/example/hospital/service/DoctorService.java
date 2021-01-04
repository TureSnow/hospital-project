package com.example.hospital.service;
import com.example.hospital.model.Patient;
import com.example.hospital.model.User;

import java.util.Date;
import java.util.List;

/**
 * doctor can only this his area patient status
 */
public interface DoctorService {

    List<Patient> getAllPatient();

    /**
     * @param lifeState  1:health;2:treating;3:dead; 4:all
     * @param isMatchWard 1:match;2:dismatch;3:all ok
     * @param IllnessLevel 0:health; 1:mild; 2:severe; 3:critical; 4:all ok
     * @return 满足筛选条件的病人
     */
    List<Patient> getPatient(int lifeState, int IllnessLevel,int isMatchWard);

    /**
     * query patient can go out form hospital
     * only mild area doctor can do this operation
     * @return  满足出院条件的病人
     */
    List<Patient> getPatientCanDischarge();

    List<Patient> getNotMatchPatient();

    //todo :把病人转入其他治疗区域
    String transferOtherArea(int patientId);

    //todo :获得单个病人信息
    Patient getPatientStateById(int patientId);

    //todo
    User getHeadNurse();

    //todo
    List<User> getWardNurses();

    //todo ：得到病房护士对应的病人->通过bed表
    List<Patient> getPatientByWardNurseId(int wardNurseId);

    //todo :修改病人病人评级
    String updatePatientIllnessLevel(int patientId, String level);

    //todo :修改病人生命状态 ->treating or death
    String updatePatientLifeStatus(int patientId, String level);

    //todo :添加核酸检测单
    String addNaSheet(int patientId, Date date,String result,String illnessLevel);

    //todo :允许病人出院
    String discharge(int patientId);

}
