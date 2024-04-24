package com.example.myproject.Implementations;

import com.example.myproject.config.Utility;
import com.example.myproject.model.User;
import com.example.myproject.security.MyUserDetails;
import com.example.myproject.service.UserNotfoundException;
import com.example.myproject.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
@Component
@RequiredArgsConstructor
@Slf4j
public class PasswordImpl {
    private final UserService userService;
    private final SenderEmail sendEmail;

    /**
     * Process the forget password form
     * @param request the HTTP request
     * @param model the data model
     */
    public void processForgetPasswordForm(HttpServletRequest request, Model model){
        String email=request.getParameter("email");
        String token= RandomString.make(45);
        try {
            userService.updateRestPassword(token,email);
            String restPasswordLink= Utility.getUrlSite(request)+"/reset_password?token="+token;
            sendEmail.sendEmailByForgetPassword(email,restPasswordLink);
            model.addAttribute("message","We have send a reset password link to your email. Please Check ");
            log.warn("The user successfully sent the email: "+email);
        } catch (UserNotfoundException e) {
            log.error("The user could not send e-mail: "+UserNotfoundException.class);
            model.addAttribute("error",e.getMessage());
        } catch (UnsupportedEncodingException | MessagingException e) {
            log.error("The user could not send e-mail: "+UnsupportedEncodingException.class+MessagingException.class);
            model.addAttribute("error","Error with sending email.");
        }
    }

    /**
     * Show the reset password page
     * @param token the token for verification
     * @param model the data model
     * @return the page name as a string
     */
    public String showRestPassword(@Param(value = "token") String token, Model model){
        log.debug("The user used a valid token: "+token);
        User user=userService.get(token);
        if (user==null){
            model.addAttribute("title","Reset your password");
            model.addAttribute("message","Invalid Token");
            log.error("Token is Incorrect");
            return "/error/403";
        }else {
            model.addAttribute("token", token);
            model.addAttribute("PageTitle", "Reset Your Password");
            log.debug("That's right Token");
            return "/reset_password";
        }
    }

    /**
     * Reset the password and login
     * @param request the HTTP request
     * @param model the data model
     * @return the page name as a string
     */
    public String resetPasswordGetLogin(HttpServletRequest request,Model model) {
        log.info("User successfully logged in to Reset Password");
        String token = request.getParameter("token");
        String password = request.getParameter("password");
        User user = userService.get(token);
        if (user == null) {
            model.addAttribute("title", "Reset your password");
            model.addAttribute("message", "Invalid Token");
            log.error("The user could not change their password");
        } else {
            userService.updatePassword(user, password, token);
            model.addAttribute("message", "You have successfully changed your password");
            log.debug("User password changed successfully");
        }
        return "/error/403";
    }

    @Component
    public static class ChangePassword {
        @Autowired
        UserService userService;

        @Autowired
        private SenderEmail senderEmail;
        @Autowired
        PasswordEncoder passwordEncoder;

        /**
         * Process changing the user password
         * @param request the HTTP request
         * @param response the HTTP response
         * @param model the data model
         * @param authentication the user authentication
         * @return the page name as a string
         */
        public String processChangePassword(HttpServletRequest request, HttpServletResponse response
                , Model model, Authentication authentication){
            MyUserDetails userDetails= (MyUserDetails) authentication.getPrincipal();
            User user=userDetails.getUser();
            String oldPassword=request.getParameter("oldPassword");
            String newPassword=request.getParameter("newPassword");
            if (!passwordEncoder.matches(oldPassword,user.getPassword())){
                model.addAttribute("pageTitle","Change your expired password");
                model.addAttribute("message","Your new password must be different the old one!");
                return "change_password";
            }
            if (oldPassword.equals(newPassword)){
                model.addAttribute("pageTitle","Change your expired password");
                model.addAttribute("message","Your old password is incorrect!");
                return "change_password";
            }
            userService.updatePassword(user,newPassword);
            model.addAttribute("pageTitle","Login again");
            model.addAttribute("message","You have changed your password successfully. Please Login again");
            return "login";
        }

        /**
         * Verify and update user email
         * @param oldEmail the current email
         * @param email the new email
         * @param request the HTTP request
         * @return the page name as a string
         */
        public String verifyEmail(String oldEmail, String email, HttpServletRequest request) {
            User user = userService.findByEmail(oldEmail);
            String randomCode = RandomString.make(64);
            user.setVerificationCode(randomCode);
            user.setEnable(false);
            user.setEmail(email);
            userService.registrationUser(user);
            String url = Utility.getUrlSite(request);
            user.setEmail(email);
            try {
                senderEmail.sendEmailByVerificationEmail(user, url, randomCode);
            } catch (MessagingException | UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
            return "register_success";
        }
    }
}