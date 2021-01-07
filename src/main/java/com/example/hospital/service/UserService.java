package com.example.hospital.service;

import com.example.hospital.model.Notify;
import com.example.hospital.model.Patient;
import com.example.hospital.model.User;
import java.util.List;

public interface UserService {
    User getUserById(int id);
    User getUserByName(String name);
    int createNewUser(User user);
    List<Notify> getUnreadNotify();
    List<Notify> getReadNotify();
    int unread2read(int id);
    List<Patient> getRecoverPatient();
    /**
     * 筛选满足条件的病人
     * @param lifeState  0:health; 1:treating; 2:dead; 3:all
     * @param isMatchWard 0:match; 1:not match; 2:all ok
     * @param IllnessLevel 0:health; 1:mild; 2:severe; 3:critical; 4:all ok
     * @return
     */
    List<Patient> getPatient(int lifeState, int IllnessLevel,int isMatchWard);
}
