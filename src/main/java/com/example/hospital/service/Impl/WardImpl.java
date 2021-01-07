package com.example.hospital.service.Impl;

import com.example.hospital.dao.*;
import com.example.hospital.model.*;
import com.example.hospital.service.WardNurseService;
import com.example.hospital.utils.NotificationUtil;
import com.example.hospital.utils.StringCheckUtil;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
@Service
public class WardImpl implements WardNurseService {
    private UserServiceImpl userService;
    private UserMapper userMapper;
    private PatientMapper patientMapper;
    private BedMapper bedMapper;
    private DailyStatusMapper dailyStatusMapper;
    private NotifyMapper notifyMapper;
    public WardImpl(UserServiceImpl userService, UserMapper userMapper, PatientMapper patientMapper, BedMapper bedMapper, DailyStatusMapper dailyStatusMapper, NotifyMapper notifyMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
        this.patientMapper = patientMapper;
        this.bedMapper = bedMapper;
        this.dailyStatusMapper = dailyStatusMapper;
        this.notifyMapper = notifyMapper;
    }
    public String getArea(){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.getUserByName(username);
        return user.getArea();
    }
    public int getUserId(){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.getUserByName(username);
        return user.getId();
    }
    @Override
    public String addDailyStatus(int patientId, Date date, float temperature, int lifeState,
                                 int na_result, String symptom) {
        BedExample example = new BedExample();
        example.or().andPatientIdEqualTo(patientId).andNurseIdEqualTo(getUserId());
        if(bedMapper.selectByExample(example).size()==0){
            return "The patient is not in the charge of the nurse";
        }
        if (!StringCheckUtil.checkString(na_result+"","01")){
            return "param error:na_result";
        }
        if (!StringCheckUtil.checkString(lifeState+"","012")){
            return "param error:life state";
        }
        DailyStatus dailyStatus = new DailyStatus();
        dailyStatus.setDate(date);
        dailyStatus.setTemperature(temperature);
        dailyStatus.setPatientId(patientId);
        dailyStatus.setLifeState(lifeState+"");
        dailyStatus.setSymptom(symptom);
        dailyStatus.setNaResult(na_result+"");
        dailyStatusMapper.insert(dailyStatus);

        DailyStatusExample dailyStatusExample = new DailyStatusExample();
        dailyStatusExample.or().andPatientIdEqualTo(patientId);
        List<DailyStatus> dailyStatuses = dailyStatusMapper.selectByExample(dailyStatusExample);
        if (dailyStatuses.size()>3){
            int last=dailyStatuses.size()-1;
            int mark = 0;
            if (dailyStatuses.get(last).getTemperature()<37.3&&dailyStatuses.get(last-1).getTemperature()<37.3
                    &&dailyStatuses.get(last-2).getTemperature()<37.3){
                mark = 1;
                Patient patient = patientMapper.selectByPrimaryKey(patientId);
                //设置为可以出院的健康状态
                patient.setLifeState("0");
                patientMapper.updateByPrimaryKey(patient);
                //给本区域的医生发出一条消息
                UserExample example1=new UserExample();
                example1.or().andAreaEqualTo(getArea()).andRoleEqualTo("0");
                User user = userMapper.selectByExample(example1).get(0);
                Integer doctorId = user.getId();
                Notify patientHealthNotify = NotificationUtil.getPatientHealthNotify(doctorId, patient.getName(), new Date());
                notifyMapper.insert(patientHealthNotify);
            }
        }
        return null;
    }

    /**
     *  根据条件筛选
     * @param canDischarge 0:can discharge 1：can't discharge
     * @param illnessLevel 0:health 1:mild 2:severe 3:critical 4:all ok
     * @return 筛选出满足条件的病人
     */
    @Override
    public List<Patient> getPatient(int canDischarge, int illnessLevel) {
        BedExample example = new BedExample();
        example.or().andNurseIdEqualTo(getUserId()).andPatientIdIsNotNull();
        List<Bed> beds = bedMapper.selectByExample(example);
        if (beds.size()==0){
            return null;
        }
        List<Patient> result=new ArrayList<>();
        if(canDischarge == 0){
            for (Bed bed:beds){
                int patientId = bed.getPatientId();
                Patient patient = patientMapper.selectByPrimaryKey(patientId);
                if (patient.getLifeState().equals("0")){
                    //health:can discharge
                    result.add(patient);
                }
            }
            return result;
        }else {
            //不能出院
            if (illnessLevel==4){
                for (Bed bed:beds){
                    int patientId = bed.getPatientId();
                    Patient patient = patientMapper.selectByPrimaryKey(patientId);
                    if (!patient.getLifeState().equals("0")){
                        result.add(patient);
                    }
                }
                return result;
            }else {
                for (Bed bed:beds){
                    int patientId = bed.getPatientId();
                    Patient patient = patientMapper.selectByPrimaryKey(patientId);
                    if (Integer.parseInt(patient.getIllnessLevel())==illnessLevel&&(!patient.getLifeState().equals("0"))){
                        result.add(patient);
                    }
                }
                return result;
            }
        }
    }

    @Override
    public List<Patient> getAllPatient() {
        BedExample example = new BedExample();
        example.or().andNurseIdEqualTo(getUserId()).andPatientIdIsNotNull();
        List<Bed> beds = bedMapper.selectByExample(example);
        List<Patient> patients=new ArrayList<>();
        for (Bed bed:beds){
            patients.add(patientMapper.selectByPrimaryKey(bed.getPatientId()));
        }
        return patients;
    }
}
