package com.example.hospital.service;

import com.example.hospital.JWT.JwtTokenUtil;
import com.example.hospital.dto.MyUserDetails;
import com.example.hospital.exception.LoginException;
import com.example.hospital.exception.NullFieldException;
import com.example.hospital.exception.OccupiedUsernameException;
import com.example.hospital.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class LoginService {
    private static final Logger LOGGER = LoggerFactory.getLogger(LoginService.class);
    private final UserDetailsService userDetailsService;
    private final UserService userService;
    private final JwtTokenUtil jwtTokenUtil;
    private final PasswordEncoder passwordEncoder;
    @Value("${jwt.tokenHead}")
    private String tokenHead;
    private final UserFormatChecker userFormatChecker = new UserFormatChecker();
    public LoginService(UserDetailsService userDetailsService, UserService userService, JwtTokenUtil jwtTokenUtil, PasswordEncoder passwordEncoder) {
        this.userDetailsService = userDetailsService;
        this.userService = userService;
        this.jwtTokenUtil = jwtTokenUtil;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * @param username 用户名
     * @param password 密码
     * @return 登陆成功的 tokenMap，登陆失败时将返回 Null
     */
    public Map<String, String> login(String username, String password) throws LoginException {
        String token;
        Map<String, String> tokenMap = new HashMap<>();
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        if (userDetails.getUsername()==null||!passwordEncoder.matches(password, userDetails.getPassword())) {
            LOGGER.error("用户名或密码不正确");
            throw new LoginException("用户名或密码不正确");
        }
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        token = jwtTokenUtil.generateToken(userDetails);
        tokenMap.put("userId", String.valueOf(((MyUserDetails)userDetails).getId()));
        tokenMap.put("token", token);
        tokenMap.put("tokenHead", tokenHead);
        return tokenMap;
    }

    public void logout(){
        SecurityContextHolder.getContext().getAuthentication().setAuthenticated(false);
    }

    public User register(User user)throws OccupiedUsernameException, NullFieldException {
        //查询是否有相同用户名的用户
        //将密码进行加密操作
        userFormatChecker.checkUserFormat(user);
        String encodePassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodePassword);
        int id = userService.createNewUser(user);
        user.setId(id);
        return user;
    }
    /**
     * 进行 user 格式的校验
     */
    private class UserFormatChecker{

        /**
         * 执行所有 check
         * @param user 需要 check 的 user
         */
        public void checkUserFormat (User user) throws NullFieldException ,OccupiedUsernameException {
            checkAllFieldRequiredNotNull(user);
            checkUsernameNotOccupied(user.getName());
        }
        /**
         * 检验用户名是否已被占用
         * @param userName username 字段
         */
        public void checkUsernameNotOccupied(String userName) throws OccupiedUsernameException {
            User userByUsername = userService.getUserByName(userName);
            if(userByUsername != null){
                throw new OccupiedUsernameException("用户名已被注册");
            }
        }

        /**
         * 检测注册需要的信息是否均不为空
         * @param user 提供的 user
         */
        public void checkAllFieldRequiredNotNull(User user) throws NullFieldException {
            if(user.getName() == null || user.getName().equals("")){
                throw new NullFieldException("name null");
            }
            if(user.getPassword() == null || user.getPassword().equals("")){
                throw new NullFieldException("password null");
            }
            if(user.getArea() == null || user.getArea().equals("")){
                throw new NullFieldException("area null");
            }
            if(user.getGender() == null || user.getGender().equals("")){
                throw new NullFieldException("gender null");
            }
            if(user.getRole() == null || user.getRole().equals("")){
                throw new NullFieldException("role null");
            }
        }
    }

}
