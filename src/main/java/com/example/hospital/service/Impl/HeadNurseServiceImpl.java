package com.example.hospital.service.Impl;

import com.example.hospital.dao.*;
import com.example.hospital.model.*;
import com.example.hospital.service.HeadNurseService;
import com.example.hospital.utils.NotificationUtil;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
@Service
public class HeadNurseServiceImpl implements HeadNurseService {
    private UserServiceImpl userService;
    private UserMapper userMapper;
    private PatientMapper patientMapper;
    private BedMapper bedMapper;
    private NaSheetMapper naSheetMapper;
    private NotifyMapper notifyMapper;
    public HeadNurseServiceImpl(UserServiceImpl userService, UserMapper userMapper, PatientMapper patientMapper, BedMapper bedMapper, NaSheetMapper naSheetMapper, NotifyMapper notifyMapper){
        this.userService = userService;
        this.userMapper = userMapper;
        this.patientMapper = patientMapper;
        this.bedMapper = bedMapper;
        this.naSheetMapper = naSheetMapper;
        this.notifyMapper = notifyMapper;
    }
    /**
     * @return now head nurse area
     */
    public String getArea(){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.getUserByName(username);
        return user.getArea();
    }
    public int getId(){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.getUserByName(username);
        return user.getId();
    }
    public void handFree(){
        //查看隔离区是否有病人等待住院
        PatientExample example1 = new PatientExample();
        example1.or().andIllnessLevelEqualTo(getArea()).andAreaLevelEqualTo("0");
        List<Patient> isoPatients = patientMapper.selectByExample(example1);
        List<Bed> freeBed;
        int headId = getId();
        if (!(isoPatients.size() ==0)){
            //there are some isolation patient
            //get all free bed
            freeBed = getFreeBed();
            int mark = Math.min(isoPatients.size(),freeBed.size());
            for (int i = 0; i < mark; i++) {
                Patient isoPatient = isoPatients.get(i);
                Bed nowBed = freeBed.get(i);
                isoPatient.setAreaLevel(getArea());
                nowBed.setPatientId(isoPatient.getId());
                patientMapper.updateByPrimaryKey(isoPatient);
                bedMapper.updateByPrimaryKey(nowBed);
                //每次进入一位病人，添加一条消息
                Notify notify = NotificationUtil.getPatientInNotify(headId, isoPatient.getName(), new Date());
                notifyMapper.insert(notify);
            }
        }
        freeBed = getFreeBed();
        if (freeBed.size()==0){
            //no free bed
            return;
        }
        //from other area select patients enter this area
        PatientExample example = new PatientExample();
        example.or().andIllnessLevelEqualTo(getArea()).andAreaLevelNotEqualTo(getArea()).andAreaLevelNotEqualTo("4");
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
            Notify notify = NotificationUtil.getPatientInNotify(headId, otherPatient.getName(), new Date());
            notifyMapper.insert(notify);
        }

    }
    public List<Bed> getFreeBed() {
        BedExample example = new BedExample();
        example.or().andLevelEqualTo(getArea()).andPatientIdIsNull().andNurseIdIsNotNull();
        return bedMapper.selectByExample(example);
    }
    @Override
    public List<Patient> getAllPatient() {
        String area = getArea();
        PatientExample example = new PatientExample();
        example.or().andAreaLevelEqualTo(area);
        return patientMapper.selectByExample(example);
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
    public String addWardNurse(String wardNurseName) {
        User user=userService.getUserByName(wardNurseName);
        if (user==null){
            return "no that user";
        }
        if (!user.getRole().equals("2")){
            return "the user isn't ward nurse";
        }
        List<Patient> ward2Patient = getPatientByWardNurseId(user.getId());
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
                    bed.setNurseId(user.getId());
                    a+=1;
                    bedMapper.updateByPrimaryKey(bed);
                    if (a>=mark)
                        break;
                }
                handFree();
            }
            userMapper.updateByPrimaryKey(user);
            return "add ok! now "+user.getName()+" belongs to "+user.getArea();
        }
        return "failed!the ward nurse has patient need to care about.";
    }

    @Override
    public String deleteWardNurse(int wardNurseId) {
        User user = userService.getUserById(wardNurseId);
        if (user==null){
            return "no that user";
        }
        if (!user.getRole().equals("2")){
            return "the user isn't ward nurse";
        }
        List<Patient> ward2Patient = getPatientByWardNurseId(user.getId());
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
