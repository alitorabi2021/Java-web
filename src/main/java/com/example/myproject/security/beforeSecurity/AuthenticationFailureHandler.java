package com.example.myproject.security.beforeSecurity;

import com.example.myproject.model.User;
import com.example.myproject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UrlPathHelper;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
@Component
public class AuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Autowired
    private UserService customerService;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response, AuthenticationException exception)
            throws IOException, ServletException {

        String email = request.getParameter("email");
        String error = exception.getMessage();
        System.out.println("A failed login attempt with email: "
                + email + ". Reason: " + error);

        super.setDefaultFailureUrl("/login?error");
        super.onAuthenticationFailure(request, response, exception);
    }

}


//
//    @Override
//    public void onAuthenticationFailure(HttpServletRequest request,
//                                        HttpServletResponse response, AuthenticationException exception)
//            throws IOException, ServletException {
//
//        String email = request.getParameter("email");
//        System.out.println("onAuthenticationFailure email: " + email);
//        request.setAttribute("email", email);
//
//        String redirectURL = "/login?error&email=" + email;
//
//        if (exception.getMessage().contains("OTP")) {
//            redirectURL = "/login?otp=true&email=" + email;
//        } else {
//            User customer = customerService.findByEmail(email);
//            if (customer.isOTPRequired()) {
//                redirectURL = "/login?otp=true&email=" + email;
//            }
//        }
//        float a=0.43f;
//        request.setAttribute("otp",a);
//        isEnable();
//        super.setDefaultFailureUrl(redirectURL);
//        super.onAuthenticationFailure(request, response, exception);
//    }
//
//}