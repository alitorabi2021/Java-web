package com.example.myproject.security;

import com.example.myproject.model.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

@Component
public class PasswordExpiredFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest servletRequest = (HttpServletRequest) request;
        String requestURL = servletRequest.getRequestURL().toString();
            if (requestURL.endsWith("/change_password")) {
                chain.doFilter(request, response);
                return;
            }


            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            Object principal = null;
            if (authentication != null) {
                principal = authentication.getPrincipal();
            }
            if (principal != null && principal instanceof MyUserDetails) {
                MyUserDetails userDetails = (MyUserDetails) principal;
                User user = userDetails.getUser();
                if (user.isPasswordExpired()) {
                    System.out.println("User: " + user.getFirstName() + user.getLastName() + ": Password Expired");
                    System.out.println("Last Time Password Changed: " + user.getPasswordChangeTime());
                    System.out.println("User Time: " + new Date());

                    HttpServletResponse servletResponse = (HttpServletResponse) response;
                    String redirectURL = servletRequest.getContextPath() + "/change_password";
                    if (!response.isCommitted()) {
                        servletResponse.sendRedirect(redirectURL);
                    }


                } else {
//                    System.out.println("User: " + user.getFirstName() + user.getLastName() + ": Password Not Expired");
                    chain.doFilter(request, response);
                }
            } else {
    //                System.out.println("PasswordExpirationFilter: " + requestURL);
                chain.doFilter(request, response);
            }

        }
    }


