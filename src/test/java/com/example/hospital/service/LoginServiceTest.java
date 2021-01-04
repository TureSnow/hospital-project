package com.example.hospital.service;

import com.example.hospital.exception.NullFieldException;
import com.example.hospital.exception.OccupiedUsernameException;
import com.example.hospital.exception.RegisterException;
import com.example.hospital.model.User;
import com.example.hospital.utils.StringCheckUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class LoginServiceTest {
    @Autowired
    private LoginService loginService;
    @Test
    void register() throws NullFieldException, OccupiedUsernameException {
        List<User> users=new ArrayList<>();
  //doctor:
//        users.add(getUser("doctor1","doctor","1","0"));
//        users.add(getUser("ftang","123456","2","0"));
//        users.add(getUser("jinax","123456","3","0"));
    //head nurse:
//        users.add(getUser("hnurse1","hnurse","1","1"));
//        users.add(getUser("anna","123456","2","1"));
//        users.add(getUser("david","123456","3","1"));
    //ward nurse:
//        users.add(getUser("wnurse1","wnurse","1","2"));
//        users.add(getUser("kris","123456","1","2"));
//        users.add(getUser("rose","123456","1","2"));

//        users.add(getUser("tina","123456","2","2"));
//        users.add(getUser("peter","123456","2","2"));

//        users.add(getUser("alice","123456","3","2"));
//        users.add(getUser("ilana","123456","3","2"));
//emergency nurse:
//        users.add(getUser("enurse1","enurse","0","3"));


        if (loginService == null){
            System.out.println("login null");
            return;
        }

        try {
            for (User user:users){
                loginService.register(user);
            }
        } catch (RegisterException e) {
            e.printStackTrace();
        }



    }
    public User getUser(String name, String password,String area,String role){
        User user = new User();
        user.setName(name);
        user.setPassword(password);
        user.setRole(role);
        user.setArea(area);
        return user;
    }
}