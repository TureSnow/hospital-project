package com.example.hospital.controller;

import com.example.hospital.api.CommonResult;
import com.example.hospital.model.Bed;
import com.example.hospital.model.Patient;
import com.example.hospital.service.HeadNurseService;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author ftang
 */
@RestController
@RequestMapping("/head")
@PreAuthorize("hasAnyRole('1')")
public class HeadController {
    private HeadNurseService headNurseService;

    public HeadController(HeadNurseService headNurseService) {
        this.headNurseService = headNurseService;
    }
    @GetMapping("/getAllPatient")
    @ApiOperation("获得本资料区域区域的所有病人")
    public CommonResult<List<Patient>> getAllPatient(){
        List<Patient> allPatient = headNurseService.getAllPatient();
        if (allPatient.size()>0){
            return CommonResult.success(allPatient);
        }
        return CommonResult.failed("no patient now in this area!");
    }

    @PostMapping("/filter")
    @ApiOperation("筛选本治疗区域的所有病人")
    public CommonResult<List<Patient>> filter(@RequestBody Map<String,Integer>param){
        List<Patient> Patient = headNurseService.getPatient(param.get("lifeState"),param.get("illnessLevel"),param.get("isMatch"));
        if (Patient.size()>0){
            return CommonResult.success(Patient);
        }
        return CommonResult.failed("no patient in your condition!");
    }
    @PostMapping("/getPatientByWard")
    @ApiOperation("查看单个护士照顾的病人")
    public CommonResult<List<Patient>> getPatientByWard(@RequestBody Map<String,Integer> param){
        List<Patient> wardNurse2Patient = headNurseService.getPatientByWardNurseId(param.get("wardNurseId"));
        if (wardNurse2Patient==null||wardNurse2Patient.size()==0){
            return CommonResult.failed("no patient");
        }
        return CommonResult.success(wardNurse2Patient);
    }

    @PostMapping("/addNurse")
    @ApiOperation("将已有的user添加为本区域的病人")
    public CommonResult<String> addWardNurse(@RequestBody Map<String,String> param){
        String msg = headNurseService.addWardNurse(param.get("wardNurseName"));
        return CommonResult.success(msg);
    }
    @PostMapping("/deleteNurse")
    @ApiOperation("将病房护士移除本区域")
    public CommonResult<String> deleteNurse(@RequestBody Map<String,String> param){
        String msg = headNurseService.deleteWardNurse(param.get("wardNurseName"));
        return CommonResult.success(msg);
    }
    @GetMapping("/allBed")
    @ApiOperation("得到本区域所有的病床信息")
    public CommonResult<List<Bed>> getAllBed(){
        List<Bed> allBed = headNurseService.getAllBed();
        return CommonResult.success(allBed);
    }
    @PostMapping("/bed2patient")
    @ApiOperation("得到特定的病床上的病人")
    public CommonResult<Patient> getPatientByPatient(@RequestBody Map<String, Integer> param){
        Patient bed2patient = headNurseService.getPatientByBed(param.get("bedId"));
        if (bed2patient==null){
            return CommonResult.failed();
        }
        return CommonResult.success(bed2patient);
    }

}
