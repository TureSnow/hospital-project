package com.example.hospital.service.Impl;

import com.example.hospital.dao.BedMapper;
import com.example.hospital.dao.NaSheetMapper;
import com.example.hospital.dao.PatientMapper;
import com.example.hospital.dao.UserMapper;
import com.example.hospital.model.*;
import com.example.hospital.service.DoctorService;
import com.example.hospital.service.UserService;
import com.example.hospital.utils.StringCheckUtil;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
@Service
public class DoctorServiceImpl implements DoctorService {
    private UserService userService;
    private UserMapper userMapper;
    private PatientMapper patientMapper;
    private BedMapper bedMapper;
    private NaSheetMapper naSheetMapper;
    public DoctorServiceImpl(UserService userService, UserMapper userMapper, PatientMapper patientMapper, BedMapper bedMapper, NaSheetMapper naSheetMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
        this.patientMapper = patientMapper;
        this.bedMapper = bedMapper;
        this.naSheetMapper = naSheetMapper;
    }

    /**
     * @return now doctor area
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

    /**
     * @param lifeState  0:health;1:treating; 2:dead; 3:all
     * @param isMatchWard 0:match;1:dismatch; 2:all ok
     * @param IllnessLevel 0:health; 1:mild; 2:severe; 3:critical; 4:all ok
     * @return 满足条件的病人
     */
    @Override
    public List<Patient> getPatient(int lifeState, int IllnessLevel,int isMatchWard){
        String area = getArea();
        PatientExample example = new PatientExample();
        example.or().andAreaLevelEqualTo(area);
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
            case 0:{
                example.or().andIllnessLevelEqualTo("0");
                break;
            }
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

    /**
     *
     * @return 满足出院条件的病人
     */
    @Override
    public List<Patient> getPatientCanDischarge() {
        String area = getArea();
        if (!area.equals("1")){
            return null;
        }
        PatientExample example = new PatientExample();
        //lifestatue = treating
        //illnesslevel = health
        //area = mild
        example.or().andLifeStateEqualTo("1").andAreaLevelEqualTo(area).andIllnessLevelEqualTo("0");
        return patientMapper.selectByExample(example);
    }


    @Override
    public List<Patient> getNotMatchPatient() {
        String area = getArea();
        List<Patient> patients = getAllPatient();
        List<Patient> result = new ArrayList<>();
        for (Patient patient:patients) {
            if (!patient.getIllnessLevel().equals(area)){
                result.add(patient);
            }
        }
        return result;
    }

    @Override
    public String transferOtherArea(int patientId) {
        Patient patient = patientMapper.selectByPrimaryKey(patientId);
        String area = patient.getAreaLevel();
        String illnessLevel = patient.getIllnessLevel();
        if (area.equals(illnessLevel))
            return "the patient don't need transfer to other area!";
        BedExample example = new BedExample();
        example.or().andLevelEqualTo(illnessLevel);
        List<Bed> beds=bedMapper.selectByExample(example);
        for(Bed bed:beds){
            if (bed.getPatientId() == null){
                //有空位可以转入
                //找到原来的床位，改为现在查到的可以转入的空位
                BedExample example1=new BedExample();
                example1.or().andPatientIdEqualTo(patient.getId());
                List<Bed> temp=bedMapper.selectByExample(example1);
                Bed prev=temp.get(0);
                prev.setPatientId(null);
                bed.setPatientId(patient.getId());
                patient.setAreaLevel(bed.getLevel());
                //查看隔离区是否有病人可以转入到该区域治疗
                PatientExample example2 = new PatientExample();
                example2.or().andIllnessLevelEqualTo(getArea()).andAreaLevelEqualTo("0");
                List<Patient> patients = patientMapper.selectByExample(example2);
                if (!(patients.size() ==0)){
                    //有，选择一位住院
                    Patient patient1=patients.get(0);
                    patient1.setAreaLevel(getArea());
                    prev.setPatientId(patient1.getId());
                    patientMapper.updateByPrimaryKey(patient1);
                    bedMapper.updateByPrimaryKey(prev);
                    bedMapper.updateByPrimaryKey(bed);
                    patientMapper.updateByPrimaryKey(patient);
                    return "transfer "+patient.getName()+" OK!And "+patient1.getName()+"enter this treatment";
                }
                bedMapper.updateByPrimaryKey(prev);
                bedMapper.updateByPrimaryKey(bed);
                patientMapper.updateByPrimaryKey(patient);
                return "transfer OK!";
            }
        }
        return "no proper bed for this patient to transfer!";
    }

    @Override
    public Patient getPatientStateById(int patientId) {
        return patientMapper.selectByPrimaryKey(patientId);
    }

    @Override
    public User getHeadNurse() {
        UserExample example = new UserExample();
        example.or().andAreaEqualTo(getArea()).andRoleEqualTo("1");
        return userMapper.selectByExample(example).get(0);
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
        List<Patient> patients=new ArrayList<>();
        for (Bed bed:beds){
            if (bed.getPatientId()!=null){
                patients.add(patientMapper.selectByPrimaryKey(bed.getPatientId()));
            }
        }
        return patients;
    }

    @Override
    public String updatePatientIllnessLevel(int patientId, String level){
        if (!StringCheckUtil.checkString(level,"0123"))
            return "param error!";
        Patient patient=patientMapper.selectByPrimaryKey(patientId);
        patient.setIllnessLevel(level);
        patientMapper.updateByPrimaryKey(patient);
        return "patient illness level change successful!";
    }

    @Override
    public String updatePatientLifeStatus(int patientId, String level) {
        if (!StringCheckUtil.checkString(level,"12"))
            return "param error！";
        Patient patient=patientMapper.selectByPrimaryKey(patientId);
        patient.setLifeState(level);
        patientMapper.updateByPrimaryKey(patient);
        return "patient life state change successful!";
    }

    @Override
    public String addNaSheet(int patientId, Date date, String result, String illnessLevel) {
        if(!StringCheckUtil.checkString(result,"01"))
            return "param error!";
        if (!StringCheckUtil.checkString(illnessLevel,"0123"))
            return "param error!";
        NaSheet naSheet = new NaSheet();
        naSheet.setDate(date);
        naSheet.setPatientId(patientId);
        naSheet.setIllnessLevel(illnessLevel);
        naSheet.setNaResult(result);
        naSheetMapper.insert(naSheet);
        return "add NA sheet ok!";
    }


    @Override
    public String discharge(int patientId) {
        String area = getArea();
        if (!area.equals("1"))
            return "Only the mild treatment area doctor can discharge patient";
        Patient patient=patientMapper.selectByPrimaryKey(patientId);
        if (!patient.getIllnessLevel().equals("0"))
            return "the patient is unhealth! discharge fail!";
        //4:home
        patient.setAreaLevel("4");
        patientMapper.updateByPrimaryKey(patient);
        BedExample example = new BedExample();
        example.or().andPatientIdEqualTo(patientId);
        List<Bed> beds = bedMapper.selectByExample(example);
        Bed bed=beds.get(0);
        bed.setPatientId(null);

        //查看隔离区是否有病人等待住院
        PatientExample example1 = new PatientExample();
        example1.or().andIllnessLevelEqualTo(getArea()).andAreaLevelEqualTo("0");
        List<Patient> patients = patientMapper.selectByExample(example1);
        if (!(patients.size() ==0)){
            //有，选择一位住院
            Patient patient1=patients.get(0);
            patient1.setAreaLevel(getArea());
            bed.setPatientId(patient1.getId());
            patientMapper.updateByPrimaryKey(patient1);
        }
        bedMapper.updateByPrimaryKey(bed);
        return "discharge ok!";
    }
}
