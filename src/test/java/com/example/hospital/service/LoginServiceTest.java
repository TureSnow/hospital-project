package com.example.hospital.service;

import com.example.hospital.exception.NullFieldException;
import com.example.hospital.exception.OccupiedUsernameException;
import com.example.hospital.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class LoginServiceTest {
    @Autowired
    private UserService userService;
    @Test
    void register() throws NullFieldException, OccupiedUsernameException {
        User user1 = new User();
        user1.setName("doctor1");
        user1.setPassword("doctor");
        user1.setArea("1");
        user1.setRole("0");

        User user2 = new User();
        user2.setName("hnurse1");
        user2.setPassword("hnurse");
        user2.setArea("1");
        user2.setRole("1");

        User user3 = new User();
        user3.setName("wnurse1");
        user3.setPassword("wnurse");
        user3.setArea("1");
        user3.setRole("2");

        User user4 = new User();
        user4.setName("enurse1");
        user4.setPassword("enurse");
        user4.setArea("0");
        user4.setRole("3");

        if (userService == null){
            System.out.println("login null");
            return;
        }
        userService.createNewUser(user1);
        userService.createNewUser(user2);
        userService.createNewUser(user3);
        userService.createNewUser(user4);

    }
}