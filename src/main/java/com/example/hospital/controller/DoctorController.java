package com.example.hospital.controller;

import com.example.hospital.api.CommonResult;
import com.example.hospital.model.Patient;
import com.example.hospital.model.User;
import com.example.hospital.service.DoctorService;
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
@RequestMapping("/user/doctor")
@PreAuthorize("hasAnyRole('0')")
public class DoctorController {
    private DoctorService doctorService;

    public DoctorController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    @GetMapping("/allPatient")
    @ApiOperation("得到该治疗区域的所有病人")
    public CommonResult<List<Patient>> getAllPatient(){
        List<Patient> allPatient = doctorService.getAllPatient();
        if (allPatient==null)
            return CommonResult.failed();
        else return CommonResult.success(allPatient);
    }

    @PostMapping("/filter")
    @ApiOperation("根据筛选得到符合筛选条件的病人")
    public CommonResult<List<Patient>> getPatient(@RequestBody Map<String,Integer> param){
        List<Patient> patients = doctorService.getPatient(param.get("lifeState"), param.get("illnessLevel"), param.get("isMatch"));
        if (patients ==null)
            return CommonResult.failed();
        else return CommonResult.success(patients);
    }
    @GetMapping("/dischargeable")
    @ApiOperation("得到符合出院条件的病人")
    public CommonResult<List<Patient>> getPatientDischarge(){
        List<Patient> patients = doctorService.getPatientCanDischarge();
        if (patients ==null)
            return CommonResult.failed();
        else return CommonResult.success(patients);
    }
    @GetMapping("/notMatch")
    @ApiOperation("得到符合转移条件的病人")
    public CommonResult<List<Patient>> getNotMatchPatient(){
        List<Patient> patients = doctorService.getNotMatchPatient();
        if (patients ==null)
            return CommonResult.failed();
        else return CommonResult.success(patients);
    }
    @PostMapping("/transfer")
    @ApiOperation("转移病人")
    public CommonResult<String> transferOtherArea(@RequestBody Map<String,Integer> param){
        String msg = doctorService.transferOtherArea(param.get("patientId"));
        return CommonResult.success(msg);
    }
    @PostMapping("/singlePatient")
    @ApiOperation("查看单个病人信息")
    public CommonResult<Patient> getPatienById(@RequestBody Map<String,Integer> param){
        Patient patient = doctorService.getPatientById(param.get("patientId"));
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
    public CommonResult<List<Patient>> getPatientByWardNurseId(@RequestBody Map<String,Integer> param){
        List<Patient> patientByWardNurseId = doctorService.getPatientByWardNurseId(param.get("wardNurseId"));
        if (patientByWardNurseId==null){
            return CommonResult.failed();
        }
        return CommonResult.success(patientByWardNurseId);
    }
    @PostMapping("/updateIllnessLeve")
    @ApiOperation("修改病人病情评级")
    public CommonResult<String> updatePatientIllnessLevel(@RequestBody Map<String,String> param){
        String msg = doctorService.updatePatientIllnessLevel(Integer.parseInt(param.get("patientId")), param.get("level"));
        return CommonResult.success(msg);
    }
    @PostMapping("/updateLifeState")
    @ApiOperation("修改病人生命状态")
    public CommonResult<String> updatePatientLifeStatus(@RequestBody Map<String,String> param){
        String msg = doctorService.updatePatientLifeStatus(Integer.parseInt(param.get("patientId")), param.get("level"));
        return CommonResult.success(msg);
    }
    @PostMapping("/addSheet")
    @ApiOperation("添加核酸检测单")
    public CommonResult<String> addNaSheet(@RequestBody Map<String,Object>param) throws ParseException {
        //将date类型的字符串转换为data类型对象
        DateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
        Date date = simpleDateFormat.parse(param.get("date").toString());

        String msg = doctorService.addNaSheet(Integer.parseInt(param.get("patientId").toString()),date,
                (String) param.get("result"), (String)param.get("illnessLevel"));
        return CommonResult.success(msg);
    }
    @PostMapping("/discharge")
    @ApiOperation("允许病人出院")
    public CommonResult<String> discharge(@RequestBody Map<String, Integer> param){
        String msg = doctorService.discharge(param.get("patientId"));
        return CommonResult.success(msg);
    }
}
