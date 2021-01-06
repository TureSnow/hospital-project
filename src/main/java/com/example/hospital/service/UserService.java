package com.example.hospital.service;


import com.example.hospital.dao.UserMapper;
import com.example.hospital.dto.MyUserDetails;
import com.example.hospital.model.Patient;
import com.example.hospital.model.User;
import com.example.hospital.model.UserExample;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author ftang
 */

@Service
public class UserService {

    private UserMapper userMapper;
    private final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    public UserService(UserMapper userMapper){
        this.userMapper = userMapper;
    }

    public User getUserById(int id){
       return userMapper.selectByPrimaryKey(id);
    }
    public User getUserByName(String name){
        UserExample example =  new UserExample();
        example.or().andNameEqualTo(name);
        List<User> users = userMapper.selectByExample(example);
        if (users.size() == 0)
            return null;
        else return users.get(0);
    }
    public int createNewUser(User user){
        String username = user.getName();
        userMapper.insert(user);
        UserExample example = new UserExample();
        example.or().andNameEqualTo(username);
        int id = userMapper.selectByExample(example).get(0).getId();
        return id;
    }

   /* public List<Patient> getAllPatientHealth(){
        MyUserDetails principal = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String area = principal.getArea();
    }*/
}
