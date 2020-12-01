package com.example.hospital.service.Impl;

import com.example.hospital.dto.CustomUserDetailSevice;
import com.example.hospital.dto.MyUserDetails;
import com.example.hospital.exception.LoginException;
import com.example.hospital.exception.RegisterException;
import com.example.hospital.model.User;
import com.example.hospital.service.LoginService;
import com.example.hospital.service.UserService;
import com.example.hospital.utils.JwtTokenUtil;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
@Service
public class LoginServiceImpl implements LoginService{
    private final CustomUserDetailSevice userDetailSevice;
    private final UserService userService;
    private final JwtTokenUtil jwtTokenUtil;
    private final PasswordEncoder passwordEncoder;
    public LoginServiceImpl(CustomUserDetailSevice userDetailSevice,UserService userService,
                            JwtTokenUtil util,PasswordEncoder encoder){
        this.userService=userService;
        this.userDetailSevice = userDetailSevice;
        this.jwtTokenUtil = util;
        this.passwordEncoder = encoder;
    }
    @Override
    public Map<String, String> login(String username, String password) throws LoginException {
        UserDetails userDetails = userDetailSevice.loadUserByUsername(username);
        if (userDetails == null||!password.equals(userDetails.getPassword())){
            throw new LoginException("username error or password error");
        }
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        Map<String,String> tokenMap = new HashMap<>();
        tokenMap.put("userId",String.valueOf(((MyUserDetails)userDetails).getId()));
        tokenMap.put("token",jwtTokenUtil.getToken(userDetails));
        return tokenMap;
    }

    @Override
    public int register(User user) throws RegisterException {
        UserDetails userDetails = userDetailSevice.loadUserByUsername(user.getName());
        if (userDetails != null){
            throw new RegisterException("name duplication!");
        }
        return userService.createNewUser(user);
    }

    @Override
    public void logout() {
        SecurityContextHolder.getContext().getAuthentication().setAuthenticated(false);
    }
}
