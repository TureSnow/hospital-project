package com.example.hospital.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hello")
public class HelloController {

    public String hello(){
        return "hello,security!";
    }
    @GetMapping("/doctor")
    @PreAuthorize("hasAnyRole('0')")
    public String helloDoctor(){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return "hello,doctor!" + username;
    }
    @GetMapping("/head")
    @PreAuthorize("hasAnyRole('1')")
    public String helloHeadNurse(){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return "hello,head nurse!";
    }
    @GetMapping("/ward")
    @PreAuthorize("hasAnyRole('2')")
    public String helloWardNurse(){
        return "hello,ward nurse!";
    }
    @GetMapping("/emergency")
    @PreAuthorize("hasAnyRole('3')")
    public String helloEmergencyNurse(){
        return "hello, emergency nurse!";
    }
}
