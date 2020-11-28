package com.example.hospital.controller;

import com.example.hospital.api.CommonResult;
import com.example.hospital.exception.LoginException;
import com.example.hospital.exception.NullFieldException;
import com.example.hospital.exception.OccupiedUsernameException;
import com.example.hospital.exception.RegisterIllegalArgumentException;
import com.example.hospital.model.User;
import com.example.hospital.service.LoginService;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class LoginController {
    private final LoginService loginService;
    @Value("${jwt.tokenHeader}")
    private String tokenHeader;
    @Value("${jwt.tokenHead}")
    private String tokenHead;
    private static final Logger LOGGER = LoggerFactory.getLogger(LoginController.class);
    public LoginController(LoginService loginService){
        this.loginService = loginService;
    }
    @ApiOperation(value = "用户注册")
    @PostMapping(value = "/register")
    public CommonResult<User> register(User user)  {
        User user1;
        try{
            user1 = loginService.register(user);
        }catch (OccupiedUsernameException | NullFieldException e){
            LOGGER.debug("register fail:{}",user);
            return CommonResult.failed(e.getMessage());
        }
        LOGGER.debug("register success:{}",user);
        return CommonResult.success(user1);
    }

    @ApiOperation(value = "登录以后返回token")
    @PostMapping(value = "/login")
    public CommonResult<Map<String,String>> login(String username, String password) {
        Map<String,String> tokenMap;
        try{
            tokenMap = loginService.login(username, password);
        }catch (LoginException e){
            LOGGER.debug("login fail:{}",username);
            return CommonResult.validateFailed(e.getMessage());
        }
        LOGGER.debug("login success:{}",username);
        return CommonResult.success(tokenMap);
    }

    @ApiOperation(value = "登出")
    @PostMapping(value = "/logout")
    public CommonResult<String> logOut() {
        loginService.logout();
        LOGGER.debug("logOut success");
        return CommonResult.success("登出成功");
    }
}
