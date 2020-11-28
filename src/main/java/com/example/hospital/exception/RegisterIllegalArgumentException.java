package com.example.hospital.exception;

public class RegisterIllegalArgumentException extends Exception{
    private String message;
    public RegisterIllegalArgumentException(String message){
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
