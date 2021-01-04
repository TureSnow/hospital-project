package com.example.hospital.service.Impl;

import com.example.hospital.dao.BedMapper;
import com.example.hospital.dao.PatientMapper;
import com.example.hospital.model.Bed;
import com.example.hospital.model.BedExample;
import com.example.hospital.model.Patient;
import com.example.hospital.model.PatientExample;
import com.example.hospital.service.EmergencyNurseService;
import com.example.hospital.service.UserService;
import com.example.hospital.utils.StringCheckUtil;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class EmergencyImpl implements EmergencyNurseService {
    private PatientMapper patientMapper;
    private BedMapper bedMapper;
    public EmergencyImpl(UserService userService,PatientMapper patientMapper, BedMapper bedMapper){
        this.patientMapper = patientMapper;
        this.bedMapper = bedMapper;
    }
    @Override
    public String addPatient(String name, String illnessLevel) {
        Patient patient=new Patient();
        patient.setName(name);
        if (!StringCheckUtil.checkString(illnessLevel,"123")){
            return "illness level not valid!";
        }
        patient.setIllnessLevel(illnessLevel);
        patient.setLifeState("1");//treating
        BedExample example = new BedExample();
        example.or().andLevelEqualTo(illnessLevel).andPatientIdIsNull().andNurseIdIsNotNull();
        List<Bed> beds = bedMapper.selectByExample(example);
        if (beds.size()==0){
            //无空位，将其安排在隔离区
            patient.setAreaLevel("0");
            patientMapper.insert(patient);
        }else {
            //有空位
            patient.setAreaLevel(illnessLevel);
            patientMapper.insert(patient);
            PatientExample example1 = new PatientExample();
            example1.or().andNameEqualTo(patient.getName());
            Patient patient1 = patientMapper.selectByExample(example1).get(0);

            Bed bed = beds.get(0);
            System.out.println("wardId: "+bed.getWardId()+" bed_id: "+bed.getBedId());
            bed.setPatientId(patient1.getId());
            System.out.println("patientId: "+bed.getPatientId());
            bedMapper.updateByPrimaryKey(bed);
        }

        return "add patient "+name+" ok! Now "+name+"in "+patient.getAreaLevel()+" treatment";
    }

    @Override
    public List<Patient> getAllPatient() {
        return patientMapper.selectByExample(new PatientExample());
    }

    /**
     *
     * @param area 0：isolation;1:mild;2；severe;3:critical 4:all
     * @param illnessLevel 0:health;1:mild;2severe;3:critical 4:all
     * @param lifeState 0:health;1:treating;2:death;3:all
     * @return 满足筛选条件的病人
     */
    @Override
    public List<Patient> getPateint(int area, int illnessLevel, int lifeState) {
        if (!(StringCheckUtil.checkString(area+"","01234")&&StringCheckUtil.checkString(illnessLevel+"","01234")
                &&StringCheckUtil.checkString(lifeState+"","0123"))){
            return null;
        }
        PatientExample example = new PatientExample();
        if (area!=4){
            example.or().andAreaLevelEqualTo(area+"");
        }
        if (illnessLevel!=4){
            example.or().andIllnessLevelEqualTo(illnessLevel+"");
        }
        if (lifeState!=3){
            example.or().andAreaLevelEqualTo(lifeState+"");
        }
        return patientMapper.selectByExample(example);
    }
}
