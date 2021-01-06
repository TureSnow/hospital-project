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
}
