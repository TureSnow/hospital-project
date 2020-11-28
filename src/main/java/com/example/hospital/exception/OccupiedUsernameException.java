package com.example.hospital.exception;

public class OccupiedUsernameException extends Exception{
    public OccupiedUsernameException(String message){
        super(message);
    }
}
