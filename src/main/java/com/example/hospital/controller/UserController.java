package com.example.hospital.controller;

import com.example.hospital.api.CommonResult;
import com.example.hospital.model.Notify;
import com.example.hospital.model.Patient;
import com.example.hospital.service.UserService;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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
    @PreAuthorize("hasAnyRole('0','1')")
    @PostMapping("/filter")
    @ApiOperation("根据筛选得到符合筛选条件的病人,医生，护士长共用接口")
    public CommonResult<List<Patient>> getPatient(@RequestBody Map<String,Integer> param){
        List<Patient> patients = userService.getPatient(param.get("lifeState"), param.get("illnessLevel"), param.get("isMatch"));
        if (patients ==null)
            return CommonResult.failed();
        else return CommonResult.success(patients);
    }

    @PostMapping("/unread2read")
    @ApiOperation("将notify改为已读状态")
    public CommonResult<String> unread2read(@RequestBody Map<String,Integer> param){
        userService.unread2read(param.get("notifyId"));
        return CommonResult.success("change ok");
    }
    @GetMapping("/homePatient")
    @ApiOperation("得到康复出院的病人")
    public CommonResult<List<Patient>> homePatient(){
        List<Patient> recoverPatient = userService.getRecoverPatient();
        return CommonResult.success(recoverPatient);
    }
}
