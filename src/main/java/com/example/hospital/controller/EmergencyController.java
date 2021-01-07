package com.example.hospital.controller;

import com.example.hospital.api.CommonResult;
import com.example.hospital.model.Patient;
import com.example.hospital.service.EmergencyNurseService;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author ftang
 */
@RestController
@RequestMapping("/emergency")
@PreAuthorize("hasAnyRole('3')")
public class EmergencyController {
    private EmergencyNurseService emergencyNurseService;

    public EmergencyController(EmergencyNurseService emergencyNurseService) {
        this.emergencyNurseService = emergencyNurseService;
    }
    @PostMapping("/addPatient")
    @ApiOperation("添加病人")
    public CommonResult<String> addpatient(@RequestBody Map<String,String> param){
        String msg = emergencyNurseService.addPatient(param.get("name"), param.get("illnessLevel"));
        return CommonResult.success(msg);
    }

    @GetMapping("/allPatient")
    @ApiOperation("查看所有病人")
    public CommonResult<List<Patient>> getAllPatient(){
        List<Patient> allPatient = emergencyNurseService.getAllPatient();
        return CommonResult.success(allPatient);
    }

    @PostMapping("/filter")
    @ApiOperation("筛选得到病人")
    public CommonResult<List<Patient>> getPatient(@RequestBody Map<String,Integer> param){
        List<Patient> pateints = emergencyNurseService.getPatient(
                param.get("area"),
                param.get("illnessLevel"),
                param.get("lifeState"));
        return CommonResult.success(pateints);
    }
}
