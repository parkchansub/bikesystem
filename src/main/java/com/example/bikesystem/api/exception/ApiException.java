package com.example.bikesystem.api.exception;

public class ApiException extends RuntimeException{

    private String message;
    private String success;


    public ApiException(String message) {
        this.message = message;
        this.success = "false";
    }

    @Override
    public String toString() {
        return "{" +
                "success='" + success + '\'' +
                ", rstCd='" + message +
                '}';
    }
}
