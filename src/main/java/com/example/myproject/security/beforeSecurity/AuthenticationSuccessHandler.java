package com.example.myproject.security.beforeSecurity;

import com.example.myproject.model.User;
import com.example.myproject.security.MyUserDetails;
import com.example.myproject.security.UserDetailsServiceImpl;
import com.example.myproject.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UrlPathHelper;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Slf4j
public class AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {
        String email = request.getParameter("email");
        log.info("this user: "+email+" Entered successfully");
        super.onAuthenticationSuccess(request, response, chain, authentication);
    }

}




//
//        @Override
//    public void onAuthenticationSuccess(HttpServletRequest request,
//                                        HttpServletResponse response, Authentication authentication)
//            throws IOException, ServletException {
//
//
//        MyUserDetails userDetails
//                = (MyUserDetails) authentication.getPrincipal();
//
//        User user = userDetails.getUser();
//
//        if (user.isOTPRequired()) {
//            userService.clearOTP(user);
//        }
//        super.onAuthenticationSuccess(request, response, authentication);
//    }
//
//}
