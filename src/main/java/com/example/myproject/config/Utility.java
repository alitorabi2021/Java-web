package com.example.myproject.config;

import javax.servlet.http.HttpServletRequest;

public class Utility {
    public static String getUrlSite(HttpServletRequest request){
        String urlSite=request.getRequestURL().toString();
        return urlSite.replace(request.getServletPath(),"");
    }
}
