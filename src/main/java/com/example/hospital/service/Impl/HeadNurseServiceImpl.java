package com.example.hospital.service.Impl;

import com.example.hospital.dao.BedMapper;
import com.example.hospital.dao.NaSheetMapper;
import com.example.hospital.dao.PatientMapper;
import com.example.hospital.dao.UserMapper;
import com.example.hospital.model.*;
import com.example.hospital.service.HeadNurseService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
@Service
public class HeadNurseServiceImpl implements HeadNurseService {
    private UserServiceImpl userService;
    private UserMapper userMapper;
    private PatientMapper patientMapper;
    private BedMapper bedMapper;
    private NaSheetMapper naSheetMapper;
    public HeadNurseServiceImpl(UserServiceImpl userService, UserMapper userMapper, PatientMapper patientMapper, BedMapper bedMapper, NaSheetMapper naSheetMapper){
        this.userService = userService;
        this.userMapper = userMapper;
        this.patientMapper = patientMapper;
        this.bedMapper = bedMapper;
        this.naSheetMapper = naSheetMapper;
    }
    /**
     * @return now head nurse area
     */
    public String getArea(){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.getUserByName(username);
        return user.getArea();
    }

    @Override
    public List<Patient> getAllPatient() {
        String area = getArea();
        PatientExample example = new PatientExample();
        example.or().andAreaLevelEqualTo(area);
        return patientMapper.selectByExample(example);
    }

    @Override
    public List<Patient> getPatient(int lifeState, int IllnessLevel, int isMatchWard) {
        PatientExample example = new PatientExample();
        example.or().andAreaLevelEqualTo(getArea());
        switch (lifeState) {
            case 0:{
                example.or().andLifeStateEqualTo("0");
                break;
            }
            case 1:{
                example.or().andLifeStateEqualTo("1");
                break;
            }
            case 2:{
                example.or().andLifeStateEqualTo("2");
                break;
            }
            case 3:{
                //do nothing
                break;
            }
        }
        switch (IllnessLevel) {
            case 1:{
                example.or().andIllnessLevelEqualTo("1");
                break;
            }
            case 2:{
                example.or().andIllnessLevelEqualTo("2");
                break;
            }
            case 3:{
                example.or().andIllnessLevelEqualTo("3");
                break;
            }
            case 4:
                break;
        }
        List<Patient> patients = patientMapper.selectByExample(example);
        List<Patient> result = new LinkedList<>();
        switch (isMatchWard){
            case 0:{//match
                int len=patients.size();
                for (Patient temp : patients) {
                    if (temp.getIllnessLevel().equals(temp.getAreaLevel())) {
                        result.add(temp);
                    }
                }
                return result;
            }
            case 1:{//dismatch
                int len=patients.size();
                for (Patient temp : patients) {
                    if (!temp.getIllnessLevel().equals(temp.getAreaLevel())) {
                        result.add(temp);
                    }
                }
                return result;
            }
            case 2:
                return patients;

        }
        return null;
    }

    @Override
    public List<Patient> getNotMatchPatient() {
        List<Patient> patients = getAllPatient();
        List<Patient> result = new ArrayList<>();
        for (Patient patient:patients) {
            if (!patient.getIllnessLevel().equals(getArea())){
                result.add(patient);
            }
        }
        return result;
    }

    @Override
    public List<User> getWardNurses() {
        UserExample example = new UserExample();
        example.or().andAreaEqualTo(getArea()).andRoleEqualTo("2");
        return userMapper.selectByExample(example);
    }

    @Override
    public List<Patient> getPatientByWardNurseId(int wardNurseId) {
        BedExample example=new BedExample();
        example.or().andLevelEqualTo(getArea()).andNurseIdEqualTo(wardNurseId);
        List<Bed> beds = bedMapper.selectByExample(example);
        if (beds.size()==0){
            return null;
        }
        List<Patient> patients=new ArrayList<>();
        for (Bed bed:beds){
            if (bed.getPatientId()!=null){
                patients.add(patientMapper.selectByPrimaryKey(bed.getPatientId()));
            }
        }
        return patients;
    }

    @Override
    public String addWardNurse(int wardNurse) {
        User user=userMapper.selectByPrimaryKey(wardNurse);
        if (user==null){
            return "no that user";
        }
        if (!user.getRole().equals("2")){
            return "the user isn't ward nurse";
        }
        List<Patient> ward2Patient = getPatientByWardNurseId(wardNurse);
        if (user.getArea().equals("0")||ward2Patient==null||ward2Patient.size()==0){
            user.setArea(getArea());
            BedExample example = new BedExample();
            example.or().andLevelEqualTo(getArea()).andNurseIdIsNull();
            List<Bed> beds = bedMapper.selectByExample(example);
            if (!(beds.size() ==0)){
                //有空床位安排
                int a=0;
                int mark=area2bed(getArea());
                for (Bed bed:beds){
                    bed.setNurseId(wardNurse);
                    a+=1;
                    bedMapper.updateByPrimaryKey(bed);
                    if (a>=mark)
                        break;
                }
            }
            userMapper.updateByPrimaryKey(user);
            return "add ok! now "+user.getName()+" belongs to "+user.getArea();
        }
        return "failed!the ward nurse has patient need to care about.";
    }

    @Override
    public String deleteWardNurse(int wardNurseId) {
        User user = userMapper.selectByPrimaryKey(wardNurseId);
        if (user==null){
            return "no that user";
        }
        if (!user.getRole().equals("2")){
            return "the user isn't ward nurse";
        }
        List<Patient> ward2Patient = getPatientByWardNurseId(wardNurseId);
        if (user.getArea().equals("0")||ward2Patient==null||ward2Patient.size()==0){
            user.setArea("0");
            userMapper.updateByPrimaryKey(user);
            return "delete ok! now "+user.getName()+" belongs to isolation area";
        }
        return "failed!the ward nurse has patient need to care about.";
    }

    @Override
    public List<Bed> getAllBed() {
        BedExample example=new BedExample();
        example.or().andLevelEqualTo(getArea());
        return bedMapper.selectByExample(example);
    }

    @Override
    public Patient getPatientByBed(int Bed) {
        Bed bed = bedMapper.selectByPrimaryKey(Bed);
        if (bed==null)
            return null;
        Integer patientId = bed.getPatientId();
        if (patientId==null)
            return null;
        return patientMapper.selectByPrimaryKey(patientId);
    }

    public int area2bed(String area){
        if (area.equals("1")){
            return 3;
        }else if (area.equals("2")){
            return 2;
        }else if (area.equals("3")){
            return 1;
        }
        return -1;
    }
}
