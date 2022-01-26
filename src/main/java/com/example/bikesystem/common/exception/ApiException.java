package com.example.bikesystem.common.exception;

public class ApiException extends RuntimeException{

    private int code;
    private String description;


    public ApiException(int code,String description){
        this.code = code;
        this.description = description;
    }



    @Override
    public String toString() {
        return "{" +
                "code=" + code +
                ", description='" + description + '\'' +
                '}';
    }
}
