package com.example.hospital.controller;

import com.example.hospital.api.CommonResult;
import com.example.hospital.model.Notify;
import com.example.hospital.service.UserService;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/readNotify")
    @ApiOperation("获取读过的消息")
    CommonResult<List<Notify>> getReadNotify(){
        List<Notify> readNotify = userService.getReadNotify();
        if (readNotify.size()==0){
            return CommonResult.failed("no read notify!");
        }
        return CommonResult.success(readNotify);
    }
    @GetMapping("/unreadNotify")
    @ApiOperation("获取未读过的消息")
    CommonResult<List<Notify>> getUnreadNotify(){
        List<Notify> unreadNotify = userService.getUnreadNotify();
        if (unreadNotify.size()==0){
            return CommonResult.failed("no unread notify!");
        }
        return CommonResult.success(unreadNotify);
    }
}
