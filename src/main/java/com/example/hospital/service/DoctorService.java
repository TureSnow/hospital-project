package com.example.hospital.service;
import com.example.hospital.model.Bed;
import com.example.hospital.model.NaSheet;
import com.example.hospital.model.Patient;
import com.example.hospital.model.User;

import java.util.Date;
import java.util.List;

/**
 * doctor can only this his area patient status
 */
public interface DoctorService {

    List<Patient> getAllPatient();

    void handFree();

    /**
     * 筛选满足条件的病人
     * @param lifeState  0:health; 1:treating; 2:dead; 3:all
     * @param isMatchWard 0:match; 1:not match; 2:all ok
     * @param IllnessLevel 0:health; 1:mild; 2:severe; 3:critical; 4:all ok
     * @return
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
    Patient getPatientById(int patientId);

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

    List<Bed> getFreeBed();

}
