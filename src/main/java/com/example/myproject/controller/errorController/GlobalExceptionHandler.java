package com.example.myproject.controller.errorController;

import javassist.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.JDBCConnectionException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.thymeleaf.exceptions.TemplateInputException;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {


    @ExceptionHandler({ NullPointerException.class, NotFoundException.class})
    public String handle404Error(NoHandlerFoundException ex){
        log.error(ex.getMessage(),ex);
        return "error/404";
    }

    @ExceptionHandler(JDBCConnectionException.class)
    public ModelAndView handleConnectionError(){
        return new ModelAndView("error/connection_error");

    }

    @ExceptionHandler(Exception.class)
    public String handleGeneralException(){
        return "error/general_error";
    }

    @ExceptionHandler(TemplateInputException.class)
    public String handelTemplate(){
        return "error/general_error";
    }



}
