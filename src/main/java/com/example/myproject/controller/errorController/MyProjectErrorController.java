package com.example.myproject.controller.errorController;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
@Controller
@Slf4j
public class MyProjectErrorController implements ErrorController{
    public String getErrorPath(){
        return "error";
    }
    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model){
        String errorPage="error";
        String pageTitle="Error";

        Object status=request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        if (status != null){
            Integer statusCode=Integer.valueOf(status.toString());

            if (statusCode == HttpStatus.NOT_FOUND.value()){
                pageTitle="Page Not Found";
                errorPage="error/404";
                log.error("Error 404");
            }else if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()){
                    pageTitle="Internal Server Error";
                    errorPage="error/500";
                    log.error("Error 500");
            } else if (statusCode == HttpStatus.FORBIDDEN.value()) {
                    pageTitle="Forbidden Error";
                    errorPage="error/403";
                    log.error("Error 403");
            }else if (statusCode==HttpStatus.BAD_REQUEST.value()){
                pageTitle="Bad Request Please again";
                errorPage="error/400";
                log.error("Error 400");
            }
        }

        model.addAttribute("pageTitle",pageTitle);

        return errorPage;
    }




}
