package com.example.hospital.controller;

import com.example.hospital.api.CommonResult;
import com.example.hospital.model.Patient;
import com.example.hospital.service.WardNurseService;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author ftang
 */
@RestController
@RequestMapping("/ward")
@PreAuthorize("hasAnyRole('2')")
public class WardController {
    private WardNurseService wardNurseService;

    public WardController(WardNurseService wardNurseService) {
        this.wardNurseService = wardNurseService;
    }

    @PostMapping("/addDailyStatus")
    @ApiOperation("添加病人的日常生命状态")
    public CommonResult<String> addDailyStatus(@RequestBody Map<String,Object> param) throws ParseException {
        DateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
        Date date = simpleDateFormat.parse(param.get("date").toString());
        int patientId = (Integer)param.get("patientId");
        float temperature = (Float) param.get("temperature");
        int lifeState = (Integer) param.get("lifeState");
        int na_result = (Integer) param.get("na_result");
        String symptom = (String) param.get("symptom");
        String msg = wardNurseService.addDailyStatus(patientId, date, temperature, lifeState, na_result, symptom);
        return CommonResult.success(msg);
    }
    @GetMapping("/allPatient")
    @ApiOperation("得到自己负责的病人")
    public CommonResult<List<Patient>> getAllPatient(){
        return CommonResult.success(wardNurseService.getAllPatient());
    }
    @PostMapping("/filter")
    @ApiOperation("筛选得到负责的病人")
    public CommonResult<List<Patient>> getPatient(@RequestBody Map<String,Integer> param){
        List<Patient> patient = wardNurseService.getPatient(param.get("canDischarge"), param.get("illnessLevel"));
        if (patient==null||patient.size()==0){
            return CommonResult.failed("no patient");
        }
        return CommonResult.success(patient);
    }
}
