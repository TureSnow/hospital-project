package com.example.hospital.service;

import com.example.hospital.exception.LoginException;
import com.example.hospital.exception.RegisterException;
import com.example.hospital.model.User;

import java.util.Map;

public interface LoginService {
    Map<String,String> login(String username, String password) throws LoginException;
    User register(User user) throws RegisterException;
    void logout();
}
