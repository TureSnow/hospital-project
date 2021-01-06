package com.example.hospital.service.Impl;

import com.example.hospital.dao.*;
import com.example.hospital.model.*;
import com.example.hospital.service.DoctorService;
import com.example.hospital.utils.NotificationUtil;
import com.example.hospital.utils.StringCheckUtil;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
@Service
public class DoctorServiceImpl implements DoctorService {
    private UserServiceImpl userService;
    private UserMapper userMapper;
    private PatientMapper patientMapper;
    private BedMapper bedMapper;
    private NaSheetMapper naSheetMapper;
    private NotifyMapper notifyMapper;
    public DoctorServiceImpl(UserServiceImpl userService, UserMapper userMapper, PatientMapper patientMapper, BedMapper bedMapper, NaSheetMapper naSheetMapper, NotifyMapper notifyMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
        this.patientMapper = patientMapper;
        this.bedMapper = bedMapper;
        this.naSheetMapper = naSheetMapper;
        this.notifyMapper = notifyMapper;
    }

    /**
     * @return now doctor area
     */
    public String getArea(){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.getUserByName(username);
        return user.getArea();
    }

    public void handFree(){
        //查看隔离区是否有病人等待住院
        PatientExample example1 = new PatientExample();
        example1.or().andIllnessLevelEqualTo(getArea()).andAreaLevelEqualTo("0");
        List<Patient> isoPatients = patientMapper.selectByExample(example1);
        if (!(isoPatients.size() ==0)){
            //there are some isolation patient
            //get all free bed
            List<Bed> freeBed = getFreeBed();
            int mark = Math.min(isoPatients.size(),freeBed.size());
            for (int i = 0; i < mark; i++) {
                Patient isoPatient = isoPatients.get(i);
                Bed nowBed = freeBed.get(i);
                isoPatient.setAreaLevel(getArea());
                nowBed.setPatientId(isoPatient.getId());
                patientMapper.updateByPrimaryKey(isoPatient);
                bedMapper.updateByPrimaryKey(nowBed);
                //每次进入一位病人，添加一条消息
                int headId = getHeadNurse().getId();
                Notify notify = NotificationUtil.getPatientInNotify(headId, isoPatient.getName(), new Date());
                notifyMapper.insert(notify);
            }
        }
        List<Bed> freeBed = getFreeBed();
        if (freeBed.size()==0){
            //no free bed
            return;
        }
        //from other area select a patient enter this area
        PatientExample example = new PatientExample();
        example.or().andIllnessLevelEqualTo(getArea()).andAreaLevelNotEqualTo(getArea());
        List<Patient> otherPateints = patientMapper.selectByExample(example);
        if (otherPateints.size()==0){
            return;
        }
        int mark=Math.min(otherPateints.size(),freeBed.size());
        for (int i = 0; i < mark; i++) {
            Patient otherPatient = otherPateints.get(i);
            otherPatient.setAreaLevel(getArea());
            //find the patient previous bed
            BedExample example2 = new BedExample();
            example2.or().andPatientIdEqualTo(otherPatient.getId());
            Bed prev = bedMapper.selectByExample(example2).get(0);
            Bed nowBed = getFreeBed().get(i);
            prev.setPatientId(null);
            nowBed.setPatientId(otherPatient.getId());
            patientMapper.updateByPrimaryKey(otherPatient);
            bedMapper.updateByPrimaryKey(prev);
            bedMapper.updateByPrimaryKey(nowBed);
            //每次进入一位病人，添加一条消息
            int headId = getHeadNurse().getId();
            Notify notify = NotificationUtil.getPatientInNotify(headId, otherPatient.getName(), new Date());
            notifyMapper.insert(notify);
        }

    }

    @Override
    public List<Patient> getAllPatient() {
        String area = getArea();
        PatientExample example = new PatientExample();
        example.or().andAreaLevelEqualTo(area);
        return patientMapper.selectByExample(example);
    }

    /**
     * 筛选满足条件的病人
     * @param lifeState  0:health; 1:treating; 2:dead; 3:all
     * @param isMatchWard 0:match; 1:not match; 2:all ok
     * @param IllnessLevel 0:health; 1:mild; 2:severe; 3:critical; 4:all ok
     * @return
     */
    @Override
    public List<Patient> getPatient(int lifeState, int IllnessLevel,int isMatchWard){
        String area = getArea();
        PatientExample example = new PatientExample();
        example.or().andAreaLevelEqualTo(area);
        if (lifeState!=3){
            example.or().andLifeStateEqualTo(lifeState+"");
        }
        if (IllnessLevel!=4){
            example.or().andIllnessLevelEqualTo(IllnessLevel+"");
        }
        List<Patient> patients = patientMapper.selectByExample(example);
        List<Patient> result = new LinkedList<>();
        switch (isMatchWard){
            case 0:{//match
                for (Patient temp : patients) {
                    if (temp.getIllnessLevel().equals(temp.getAreaLevel())) {
                        result.add(temp);
                    }
                }
                return result;
            }
            case 1:{//not match
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
        //lifestatue = health
        //area = mild
        example.or().andLifeStateEqualTo("0").andAreaLevelEqualTo("1");
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
    public String transferOtherArea(int patientId){
        Patient patient = patientMapper.selectByPrimaryKey(patientId);
        String area = patient.getAreaLevel();
        String illnessLevel = patient.getIllnessLevel();
        if (area.equals(illnessLevel))
            return "the patient don't need transfer to other area!";
        BedExample example = new BedExample();
        example.or().andLevelEqualTo(illnessLevel).andPatientIdIsNull();
        List<Bed> beds=bedMapper.selectByExample(example);
        if (beds.size()!=0){
            //有空位可以转入
            //找到原来的床位，改为现在查到的可以转入的空位
            Bed now =beds.get(0);
            BedExample example1=new BedExample();
            example1.or().andPatientIdEqualTo(patient.getId());
            List<Bed> temp=bedMapper.selectByExample(example1);
            Bed prev=temp.get(0);
            prev.setPatientId(null);
            now.setPatientId(patient.getId());
            patient.setAreaLevel(now.getLevel());
            bedMapper.updateByPrimaryKey(prev);
            bedMapper.updateByPrimaryKey(now);
            patientMapper.updateByPrimaryKey(patient);
            //给 now head nurse 发送一条消息
            UserExample userExample = new UserExample();
            userExample.or().andAreaEqualTo(now.getLevel()).andRoleEqualTo("1");
            User head = userMapper.selectByExample(userExample).get(0);
            int headId = head.getId();
            Notify notify1 = NotificationUtil.getPatientInNotify(headId, patient.getName(), new Date());
            notifyMapper.insert(notify1);
            handFree();
            return "transfer ok! patient: "+patient.getName()+" already transfer to area "+patient.getAreaLevel();
        }
        return "no proper bed for this patient to transfer!";
    }

    @Override
    public Patient getPatientById(int patientId) {
        //只能查看自己区域的
        Patient patient = patientMapper.selectByPrimaryKey(patientId);
        if (patient.getAreaLevel().equals(getArea()))
            return patient;
        else
            return null;
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
        if (!StringCheckUtil.checkString(level,"123"))
            return "param error!";
        Patient patient=patientMapper.selectByPrimaryKey(patientId);
        if(!patient.getAreaLevel().equals(getArea())){
            return "It's not a patient in that area!";
        }
        patient.setIllnessLevel(level);
        patientMapper.updateByPrimaryKey(patient);
        transferOtherArea(patientId);
        return "patient illness level change successful!";
    }

    @Override
    public String updatePatientLifeStatus(int patientId, String level) {
        if (!StringCheckUtil.checkString(level,"012"))
            return "param error！";
        Patient patient=patientMapper.selectByPrimaryKey(patientId);
        if(!patient.getAreaLevel().equals(getArea())){
            return "It's not a patient in that area!";
        }
        patient.setLifeState(level);
        patientMapper.updateByPrimaryKey(patient);
        return "patient life state change successful!";
    }

    @Override
    public String addNaSheet(int patientId, Date date, String result, String illnessLevel) {
        if(!StringCheckUtil.checkString(result,"01"))
            return "param error!";
        if (!StringCheckUtil.checkString(illnessLevel,"123"))
            return "param error!";
        NaSheet naSheet = new NaSheet();
        naSheet.setPatientId(patientId);
        naSheet.setDate(date);
        naSheet.setNaResult(result);
        naSheet.setIllnessLevel(illnessLevel);
        naSheetMapper.insert(naSheet);
        NaSheetExample naSheetExample = new NaSheetExample();
        naSheetExample.or().andPatientIdEqualTo(patientId);
        List<NaSheet> naSheets = naSheetMapper.selectByExample(naSheetExample);
        if (naSheets.size()>=2){
            int last = naSheets.size()-1;
            if (naSheets.get(last).getNaResult().equals("0")&&naSheets.get(last-1).getNaResult().equals("0")){
                //连续两次核酸检测为阴性，病人为健康
                Patient patient = patientMapper.selectByPrimaryKey(patientId);
                patient.setLifeState("0");
                patientMapper.updateByPrimaryKey(patient);
                UserExample example1=new UserExample();
                example1.or().andAreaEqualTo(getArea()).andRoleEqualTo("0");
                User user = userMapper.selectByExample(example1).get(0);
                Integer doctorId = user.getId();
                Notify patientHealthNotify = NotificationUtil.getPatientHealthNotify(doctorId, patient.getName(), new Date());
                notifyMapper.insert(patientHealthNotify);
            }
        }
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
        Bed prev =beds.get(0);
        prev.setPatientId(null);
        bedMapper.updateByPrimaryKey(prev);
        handFree();
        return "discharge ok!";
    }

    @Override
    public List<Bed> getFreeBed() {
        BedExample example = new BedExample();
        example.or().andLevelEqualTo(getArea()).andPatientIdIsNull().andNurseIdIsNotNull();
        return bedMapper.selectByExample(example);
    }
}
