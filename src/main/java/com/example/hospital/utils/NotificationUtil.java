package com.example.hospital.utils;

import com.example.hospital.model.Notify;

import java.util.Date;

public class NotificationUtil {
    public static Notify getPatientHealthNotify(int userId, String patientName, Date date){
        Notify notify = new Notify();
        notify.setUserId(userId);
        notify.setDate(date);
        notify.setIsRead("0");
        notify.setContent("patient: "+patientName+" is already health and can discharge form hospital");
        return notify;
    }
    public static Notify getPatientTransNotify(int userId, String patientName, Date date,String area){
        Notify notify = new Notify();
        notify.setUserId(userId);
        notify.setDate(date);
        notify.setIsRead("0");
        notify.setContent("patient: "+patientName+" is already trans to area:"+ area);
        return notify;
    }
    public static Notify getPatientInNotify(int userId,String patientName, Date date){
        Notify notify = new Notify();
        notify.setUserId(userId);
        notify.setDate(date);
        notify.setIsRead("0");
        notify.setContent("There is a new patient: "+ patientName);
        return notify;
    }
}
