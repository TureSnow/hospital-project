package com.example.hospital.controller;

import com.example.hospital.api.CommonResult;
import com.example.hospital.model.Notify;
import com.example.hospital.model.Patient;
import com.example.hospital.service.DoctorService;
import com.example.hospital.service.UserService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {
    private UserService userService;
    private DoctorService doctorService;
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
    @ApiOperation("根据筛选得到符合筛选条件的病人")
    public CommonResult<List<Patient>> getPatient(@RequestBody Map<String,Integer> param){
        List<Patient> patients = doctorService.getPatient(param.get("lifeState"), param.get("illnessLevel"), param.get("isMatch"));
        if (patients ==null)
            return CommonResult.failed();
        else return CommonResult.success(patients);
    }
}
