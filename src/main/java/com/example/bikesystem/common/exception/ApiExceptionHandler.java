package com.example.bikesystem.common.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class ApiExceptionHandler {


/*    @ExceptionHandler(Exception.class)
    @ResponseBody
    public String defaultException(Exception ex){
        return ex.toString();
    }*/


    @ExceptionHandler(ApiException.class)
    @ResponseBody
    public String ApiException(ApiException ex) {
        return ex.toString();
    }


}
