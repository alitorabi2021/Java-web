package com.example.myproject.security.oauth2;

import com.example.myproject.model.AuthenticationProvider;
import com.example.myproject.model.Role;
import com.example.myproject.model.User;

import com.example.myproject.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;


@Component
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final UserService userService;

    public OAuth2LoginSuccessHandler(UserService userService) {
        this.userService = userService;
    }
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        CustomOAuth2User oAuth2User= (CustomOAuth2User) authentication.getPrincipal();
        String email=oAuth2User.getEmail();
        System.out.println(email);
        User user=userService.findByEmail(email);
        String name=oAuth2User.getName();

        if (user ==null){
           userService.createNewCustomerAfterOAuthLoginSuccess(email,name, AuthenticationProvider.GOOGLE);

        }else {
            userService.updateCustomerAfterOAuthLoginSuccess(user,name, AuthenticationProvider.GOOGLE);
        }
        super.onAuthenticationSuccess(request, response, authentication);
    }
}
