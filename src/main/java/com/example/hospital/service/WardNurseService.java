package com.example.hospital.service;
import com.example.hospital.model.Patient;
import java.util.Date;
import java.util.List;

public interface WardNurseService {
    public String addDailyStatus(int patientId, Date date, float temperature,
                                 int lifeState,int na_result,String symptom);

    /**
     *  根据条件筛选
     * @param canDischarge
     * @param lifeState
     * @return 筛选出满足条件的病人
     */
    public List<Patient> getPatient(int canDischarge,int lifeState);

    /**
     *
     * @return 自己负责的病人
     */
    public List<Patient> getAllPatient();
}
