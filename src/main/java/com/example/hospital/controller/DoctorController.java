package com.example.hospital.controller;

import com.example.hospital.api.CommonResult;
import com.example.hospital.model.Patient;
import com.example.hospital.model.User;
import com.example.hospital.service.DoctorService;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.Date;
import java.util.List;

/**
 * @author ftang
 */
@RestController
@RequestMapping("/user/doctor")
@PreAuthorize("hasAnyRole('0')")
public class DoctorController {
    private DoctorService doctorService;
    @PostMapping("/allPatient")
    @ApiOperation("得到该治疗区域的所有病人")
    public CommonResult<List<Patient>> getAllPatient(){
        List<Patient> allPatient = doctorService.getAllPatient();
        if (allPatient==null)
            return CommonResult.failed();
        else return CommonResult.success(allPatient);
    }

    @PostMapping("/filter")
    @ApiOperation("根据筛选得到符合筛选条件的病人")
    public CommonResult<List<Patient>> getPatient(int lifeState,int illnessLevel,int isMatch){
        List<Patient> patients = doctorService.getPatient(lifeState, illnessLevel, isMatch);
        if (patients ==null)
            return CommonResult.failed();
        else return CommonResult.success(patients);
    }
    @PostMapping("/Dpatients")
    @ApiOperation("根据筛选得到符合出院条件的病人")
    public CommonResult<List<Patient>> getPatientDischarge(){
        List<Patient> patients = doctorService.getPatientCanDischarge();
        if (patients ==null)
            return CommonResult.failed();
        else return CommonResult.success(patients);
    }
    @PostMapping("/notMatch")
    @ApiOperation("根据筛选得到符合出院条件的病人")
    public CommonResult<List<Patient>> getNotMatchPatient(){
        List<Patient> patients = doctorService.getNotMatchPatient();
        if (patients ==null)
            return CommonResult.failed();
        else return CommonResult.success(patients);
    }
    @PostMapping("/transfer")
    @ApiOperation("转移病人")
    public CommonResult<String> transferOtherArea(int patientId){
        String msg = doctorService.transferOtherArea(patientId);
        return CommonResult.success(msg);
    }
    @PostMapping("/singlePatient")
    @ApiOperation("查看单个病人信息")
    public CommonResult<Patient> getPatienById(int patientId){
        Patient patient = doctorService.getPatientStateById(patientId);
        if (patient==null)
            return CommonResult.failed();
        else return CommonResult.success(patient);
    }

    @GetMapping("/head")
    @ApiOperation("查看治疗区域的护士长")
    public CommonResult<User> getHeadNurse(){
        User head=doctorService.getHeadNurse();
        if (head==null){
            return CommonResult.failed();
        }
        return CommonResult.success(head);
    }

    @GetMapping("/ward")
    @ApiOperation("查看治疗区域的病房护士")
    public CommonResult<List<User>> getWardNurses(){
        List<User> wardNurses = doctorService.getWardNurses();
        if (wardNurses==null){
            return CommonResult.failed();
        }
        return CommonResult.success(wardNurses);
    }

    @PostMapping("/ward2patients")
    @ApiOperation("查看治疗区域的病房护士负责的病人")
    public CommonResult<List<Patient>> getPatientByWardNurseId(int wardNurseId){
        List<Patient> patientByWardNurseId = doctorService.getPatientByWardNurseId(wardNurseId);
        if (patientByWardNurseId==null){
            return CommonResult.failed();
        }
        return CommonResult.success(patientByWardNurseId);
    }
    @PostMapping("/updateIllnessLeve")
    @ApiOperation("修改病人病情评级")
    public CommonResult<String> updatePatientIllnessLevel(int patientId, String level){
        String msg = doctorService.updatePatientIllnessLevel(patientId, level);
        return CommonResult.success(msg);
    }
    @PostMapping("/updateLifeState")
    @ApiOperation("修改病人生命状态")
    public CommonResult<String> updatePatientLifeStatus(int patientId, String level){
        String msg = doctorService.updatePatientLifeStatus(patientId, level);
        return CommonResult.success(msg);
    }
    @PostMapping("/addSheet")
    @ApiOperation("添加核酸检测单")
    public CommonResult<String> addNaSheet(int patientId, Date date, String result, String illnessLevel){
        String msg = doctorService.addNaSheet(patientId, date, result, illnessLevel);
        return CommonResult.success(msg);
    }
    @PostMapping("/discharge")
    @ApiOperation("允许病人出院")
    public CommonResult<String> discharge(int patientId){
        String msg = doctorService.discharge(patientId);
        return CommonResult.success(msg);
    }
}
