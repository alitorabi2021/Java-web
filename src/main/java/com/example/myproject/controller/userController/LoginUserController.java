package com.example.myproject.controller.userController;

import com.example.myproject.service.UserService;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping
@Slf4j
@RequiredArgsConstructor
public class LoginUserController {
    private final UserService userService;
    @RequestMapping("/login")
    public String getLogin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {

            return "login";

        }


        return "redirect:/";
    }
    @GetMapping("/profile")
    public String profile(){
        return "profile";
    }
    @GetMapping("/deleteUser")
    public String deleteAccount(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        userService.deleteUser(authentication.getName());
        log.info("this User :"+authentication.getName()+" Delete Account in database");
        return "redirect:/logout";
    }


}
