package com.example.hospital.service.Impl;


import com.example.hospital.dao.NotifyMapper;
import com.example.hospital.dao.PatientMapper;
import com.example.hospital.dao.UserMapper;
import com.example.hospital.model.*;
import com.example.hospital.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

/**
 * @author ftang
 */

@Service
public class UserServiceImpl implements UserService {
    private UserMapper userMapper;
    private NotifyMapper notifyMapper;
    private PatientMapper patientMapper;
    private final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);
    int getNowUserId(){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = getUserByName(username);
        return user.getId();
    }
    public UserServiceImpl(UserMapper userMapper, NotifyMapper notifyMapper, PatientMapper patientMapper){
        this.userMapper = userMapper;
        this.notifyMapper = notifyMapper;
        this.patientMapper = patientMapper;
    }
    public User getUserById(int id){
       return userMapper.selectByPrimaryKey(id);
    }
    public User getUserByName(String name){
        UserExample example =  new UserExample();
        example.or().andNameEqualTo(name);
        List<User> users = userMapper.selectByExample(example);
        if (users.size() == 0)
            return null;
        else return users.get(0);
    }
    public int createNewUser(User user){
        String username = user.getName();
        userMapper.insert(user);
        UserExample example = new UserExample();
        example.or().andNameEqualTo(username);
        int id = userMapper.selectByExample(example).get(0).getId();
        return id;
    }
    public List<Notify> getUnreadNotify(){
        int id=getNowUserId();
        NotifyExample notifyExample = new NotifyExample();
        notifyExample.or().andUserIdEqualTo(id).andIsReadEqualTo("0");
        List<Notify> notifies = notifyMapper.selectByExample(notifyExample);
        //change the unread -> read
        if (notifies.size()>0){
            for (Notify notify:notifies){
                notify.setIsRead("1");
                notifyMapper.updateByPrimaryKey(notify);
            }
        }
        return notifies;
    }
    @Override
    public List<Notify> getReadNotify() {
        int id=getNowUserId();
        NotifyExample notifyExample = new NotifyExample();
        notifyExample.or().andUserIdEqualTo(id).andIsReadEqualTo("1");
        List<Notify> notifies = notifyMapper.selectByExample(notifyExample);
        return notifies;
    }

    @Override
    public int unread2read(int id) {
        Notify notify = notifyMapper.selectByPrimaryKey(id);
        notify.setIsRead("1");
        return notifyMapper.updateByPrimaryKey(notify);
    }

    @Override
    public List<Patient> getRecoverPatient() {
        PatientExample example=new PatientExample();
        example.or().andLifeStateEqualTo("0").andAreaLevelEqualTo("4");
        return patientMapper.selectByExample(example);
    }

    @Override
    public List<Patient> getDeathPatient() {
        PatientExample example=new PatientExample();
        example.or().andLifeStateEqualTo("2").andAreaLevelEqualTo("4");
        return patientMapper.selectByExample(example);
    }

    /**
     * @return now user area
     */
    public String getArea(){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = getUserByName(username);
        return user.getArea();
    }
    /**
     * 筛选满足条件的病人
     * @param lifeState  0:health; 1:treating; 2:dead; 3:all
     * @param isMatchWard 0:match; 1:not match; 2:all ok
     * @param IllnessLevel 1:mild; 2:severe; 3:critical; 4:all ok
     * @return
     */
    @Override
    public List<Patient> getPatient(int lifeState, int IllnessLevel,int isMatchWard){
        String area = getArea();
        PatientExample example = new PatientExample();
        if (lifeState!=3){
            if (IllnessLevel!=4){
                example.or().andAreaLevelEqualTo(area)
                        .andLifeStateEqualTo(lifeState+"")
                        .andIllnessLevelEqualTo(IllnessLevel+"");
            }else {
                example.or().andAreaLevelEqualTo(area)
                        .andLifeStateEqualTo(lifeState+"");
            }
        }else {
            if (IllnessLevel!=4){
                example.or().andAreaLevelEqualTo(area)
                        .andIllnessLevelEqualTo(IllnessLevel+"");
            }else {
                example.or().andAreaLevelEqualTo(area);
            }
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
}
