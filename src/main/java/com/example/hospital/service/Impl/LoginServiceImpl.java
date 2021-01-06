package com.example.hospital.service.Impl;

import com.example.hospital.dto.CustomUserDetailSevice;
import com.example.hospital.dto.MyUserDetails;
import com.example.hospital.exception.LoginException;
import com.example.hospital.exception.RegisterException;
import com.example.hospital.model.User;
import com.example.hospital.service.LoginService;
import com.example.hospital.utils.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Value;
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
    private final UserServiceImpl userService;
    private final JwtTokenUtil jwtTokenUtil;
    private final PasswordEncoder passwordEncoder;
    @Value("${jwt.tokenHead}")
    private String tokenHead;
    public LoginServiceImpl(CustomUserDetailSevice userDetailSevice, UserServiceImpl userService,
                            JwtTokenUtil util, PasswordEncoder encoder){
        this.userService=userService;
        this.userDetailSevice = userDetailSevice;
        this.jwtTokenUtil = util;
        this.passwordEncoder = encoder;
    }
    @Override
    public Map<String, String> login(String username, String password) throws LoginException {
        String token;
        Map<String, String> tokenMap = new HashMap<>();
        UserDetails userDetails = userDetailSevice.loadUserByUsername(username);
        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new LoginException("用户名或密码不正确");
        }
        userDetails.getAuthorities().forEach(O->{
            System.out.println(O.toString());
        });
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        //返回值增加用户信息
        User user = userService.getUserByName(username);
        token = jwtTokenUtil.generateToken(userDetails);
        tokenMap.put("token", token);
        tokenMap.put("tokenHead", tokenHead);
        tokenMap.put("area",user.getArea());
        tokenMap.put("username",user.getName());
        tokenMap.put("role",user.getRole());
        tokenMap.put("id",user.getId()+"");

        return tokenMap;
    }

    @Override
    public User register(User user) throws RegisterException {
        UserDetails userDetails = userDetailSevice.loadUserByUsername(user.getName());
        if (userDetails != null){
            throw new RegisterException("name duplication!");
        }
        String encodePassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodePassword);
        int id = userService.createNewUser(user);
        user.setId(id);
        return user;
    }

    @Override
    public void logout() {
        SecurityContextHolder.getContext().getAuthentication().setAuthenticated(false);
    }
}
