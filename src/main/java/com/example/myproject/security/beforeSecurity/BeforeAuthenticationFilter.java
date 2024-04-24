package com.example.myproject.security.beforeSecurity;

import com.example.myproject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@Component
public class BeforeAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    @Override
    public AuthenticationManager getAuthenticationManager() {
        return super.getAuthenticationManager();
    }


    public BeforeAuthenticationFilter() {
        super.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher("/login", "POST"));
        super.setUsernameParameter("email");
        super.setPasswordParameter("password");
    }

    @Autowired
    @Override
    public void setRememberMeServices(RememberMeServices rememberMeServices) {
        super.setRememberMeServices(rememberMeServices);
    }

    @Autowired
    @Override
    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        super.setAuthenticationManager(authenticationManager);

    }

    @Autowired
    @Override
    public void setAuthenticationFailureHandler(@Qualifier("authenticationFailureHandler") AuthenticationFailureHandler failureHandler) {

        super.setAuthenticationFailureHandler(failureHandler);
    }

    @Override
    protected AuthenticationSuccessHandler getSuccessHandler() {
        return super.getSuccessHandler();
    }

    @Autowired
    @Override
    public void setAuthenticationSuccessHandler(@Qualifier("authenticationSuccessHandler") AuthenticationSuccessHandler successHandler) {
        super.setAuthenticationSuccessHandler(successHandler);
    }

        @Override
        public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
                throws AuthenticationException {

            String email = request.getParameter("email");

            System.out.println("The user " + email + " is about to login");

            // run custom logics...

            return super.attemptAuthentication(request, response);
        }
    }


//
//    @Override
//    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
//        String email = request.getParameter("email");
//        String password=request.getParameter("password");
//        User customer = userService.findByEmail(email);
//        float spamScore=0.63f;
//        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
//        if (customer != null) {
//            if (encoder.matches(password, customer.getPassword())) {
//                return super.attemptAuthentication(request, response);
//            } else {
//                String pass = encoder.encode(password);
//                System.out.println("password: " + pass);
//                System.out.println("pass :" + customer.getPassword());
//                if (password.equals(customer.getPassword()))
//                    System.out.println("attemptAuthentication - email: " + email);
//                 spamScore = getGoogleRecaptcha();
//                if (spamScore < 0.5) {
//                    if (customer.isOTPRequired()) {
//                        userService.clearOTP(customer);
//                        return super.attemptAuthentication(request, response);
//                    }
//                    try {
//                        userService.generatedOneTimePassword(customer);
//                        throw new InsufficientAuthenticationException("OTP");
//                    } catch (MessagingException | UnsupportedEncodingException ex) {
//                        throw new AuthenticationServiceException(
//                                "Error while sending OTP email.");
//                    }
//
//                }
//            }
//        }
//
//
//        return super.attemptAuthentication(request, response);
//    }
//    private float getGoogleRecaptcha() {
//        return 0.43f;
//    }
//}
