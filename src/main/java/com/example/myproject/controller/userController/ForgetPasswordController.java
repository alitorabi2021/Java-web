package com.example.myproject.controller.userController;


import com.example.myproject.Implementations.PasswordImpl;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping
public class ForgetPasswordController  {
    private final PasswordImpl password;

    public ForgetPasswordController(PasswordImpl password) {
        this.password = password;
    }


    @GetMapping("/forget_password")
    public String showForgetPasswordPage(Model model) {
        // Get User to Forget Password
        model.addAttribute("pageTitle", "Forget Password");
        return "forget_password";
    }
    @PostMapping("/forget_password")
    public String processForgetPasswordForm(HttpServletRequest request, Model model){
        password.processForgetPasswordForm(request,model);
        return "/forget_password";

    }
    @GetMapping("/reset_password")
    public String showRestPassword(@Param(value = "token") String token, Model model) {
        return password.showRestPassword(token,model);
    }


    @PostMapping("/reset_password")
    public String resetPasswordGetLogin(HttpServletRequest request,Model model) {
    return password.resetPasswordGetLogin(request,model);
    }





}