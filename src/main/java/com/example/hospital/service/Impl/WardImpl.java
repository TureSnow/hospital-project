package com.example.hospital.service.Impl;

import com.example.hospital.dao.BedMapper;
import com.example.hospital.dao.PatientMapper;
import com.example.hospital.model.*;
import com.example.hospital.service.UserService;
import com.example.hospital.service.WardNurseService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
@Service
public class WardImpl implements WardNurseService {
    private UserService userService;
    private PatientMapper patientMapper;
    private BedMapper bedMapper;

    public WardImpl(UserService userService, PatientMapper patientMapper, BedMapper bedMapper) {
        this.userService = userService;
        this.patientMapper = patientMapper;
        this.bedMapper = bedMapper;
    }
    public int getUserId(){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.getUserByName(username);
        return user.getId();
    }
    @Override
    public String addDailyStatus(int patientId, Date date, float temperature, int lifeState, int na_result, String symptom) {
        BedExample example = new BedExample();
        example.or().andPatientIdEqualTo(patientId).andNurseIdEqualTo(getUserId());
        if(bedMapper.selectByExample(example).size()==0){
            return "The patient is not in the charge of the nurse";
        }
        DailyStatus dailyStatus = new DailyStatus();
        dailyStatus.setDate(date);
        dailyStatus.setPatientId(patientId);
        dailyStatus.setLifeState(lifeState+"");
        return null;
    }

    /**
     *  根据条件筛选
     * @param canDischarge 0:can discharge 1：can't discharge
     * @param lifeState
     * @return 筛选出满足条件的病人
     */
    @Override
    public List<Patient> getPatient(int canDischarge, int lifeState) {
        BedExample example = new BedExample();
        example.or().andNurseIdEqualTo(getUserId()).andPatientIdIsNotNull();
        List<Bed> beds = bedMapper.selectByExample(example);
        if (beds.size()==0){
            return null;
        }

        return null;
    }

    @Override
    public List<Patient> getAllPatient() {
        return null;
    }
}
