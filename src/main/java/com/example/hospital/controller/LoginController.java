package com.example.hospital.controller;

import com.example.hospital.api.CommonResult;
import com.example.hospital.exception.LoginException;
import com.example.hospital.exception.RegisterException;
import com.example.hospital.model.User;
import com.example.hospital.service.LoginService;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class LoginController {
    private LoginService loginService;
    public LoginController(LoginService loginService){
        this.loginService=loginService;
    }
    @ApiOperation("login and return token map")
    @PostMapping("/login")
    public CommonResult<Map<String,String>> login(String username,String password){
        try {
            Map map = loginService.login(username,password);
            System.out.println(map.get("token"));
            return CommonResult.success(map);
        } catch (LoginException e) {
            e.printStackTrace();
            return CommonResult.validateFailed();
        }
    }
    @ApiOperation(value = "register, return userId")
    @PostMapping(value = "/register")
    public CommonResult<User> register(User user)  {
        try{
            User user1 = loginService.register(user);
            return CommonResult.success(user1);
        }catch (RegisterException e){
            return CommonResult.failed(e.getMessage());
        }
    }
    @ApiOperation(value = "logout")
    @PostMapping(value = "/logout")
    public CommonResult<String> logOut() {
        loginService.logout();
        /*LOGGER.debug("logOut success");*/
        return CommonResult.success("登出成功");
    }
}
