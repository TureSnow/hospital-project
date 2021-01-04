package com.example.hospital.utils;

public class StringCheckUtil {
    public static boolean checkString(String s,String allow){
        int len=allow.length();
        for (int i = 0; i < len; i++) {
            if (s.equals(allow.charAt(i)+"")){
                return true;
            }
        }
        return false;
    }
}
