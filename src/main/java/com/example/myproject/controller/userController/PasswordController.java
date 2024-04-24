package com.example.myproject.controller.userController;

import com.example.myproject.Implementations.PasswordImpl;
import com.example.myproject.Implementations.SenderEmail;
import com.example.myproject.config.Utility;
import com.example.myproject.model.User;
import com.example.myproject.security.MyUserDetails;
import com.example.myproject.service.UserService;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;

@Controller
public class PasswordController {
    @Autowired
    private PasswordImpl.ChangePassword changePassword;

    @GetMapping("/change_password")
    public String showChangePasswordPage(Model model){
        model.addAttribute("pageTitle","Change your expired password");
        return "change_password";
    }
    @GetMapping("/change_password_user")
    public String ChangePasswordPage(Model model){
        model.addAttribute("pageTitle","Change your expired password");
        return "change_password";
    }

    @PostMapping("/change_password")
    public String processChangePassword(HttpServletRequest request, HttpServletResponse response
            , Model model,  Authentication authentication){
        return changePassword.processChangePassword(request,response,model,authentication);
    }

    @RequestMapping("/editProfile")
    public String editEmail(){
    return "/editEmail";
    }

    @PostMapping("/saveEmail")
    public String verifyEmail(@Param("oldEmail")String oldEmail,
                              @ModelAttribute("email")String email, HttpServletRequest request){
        return changePassword.verifyEmail(oldEmail,email,request);
    }




}
