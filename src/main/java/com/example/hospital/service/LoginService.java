package com.example.hospital.service;

import com.example.hospital.exception.LoginException;
import com.example.hospital.exception.RegisterException;
import com.example.hospital.model.User;

import java.util.Map;

public interface LoginService {
    public Map<String,String> login(String username,String password) throws LoginException;
    public User register(User user) throws RegisterException;
    public void logout();
}
