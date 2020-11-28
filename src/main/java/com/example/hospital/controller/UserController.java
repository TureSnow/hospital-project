package com.example.hospital.controller;

import com.example.hospital.model.User;
import com.example.hospital.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author ftang
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @RequestMapping("/getUser/{id}")
    @ResponseBody
    public User getUserById(@PathVariable(value = "id")int id){
        return userService.getUserById(id);
    }

    @RequestMapping("/newUser/{user}")
    @ResponseBody
    public int newUser(@PathVariable(value = "user")User user){
        int id  = userService.createNewUser(user);
        return id;
    }

}
