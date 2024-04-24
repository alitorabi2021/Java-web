package com.example.myproject.controller.userController;

import com.example.myproject.config.RecaptchaResponse;
import com.example.myproject.config.Utility;
import com.example.myproject.Implementations.SenderEmail;
import com.example.myproject.model.User;
import com.example.myproject.security.MyUserDetails;
import com.example.myproject.service.UserService;
import lombok.RequiredArgsConstructor;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@RequiredArgsConstructor
@RequestMapping
public class SignupUserController {

    private final SenderEmail senderEmail;

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder){
        return builder.build();
    }
    @Autowired
    private  RestTemplate restTemplate;

    private final UserService userService;
    @Value("${recaptcha.secret}")
    private String recaptchaSecret;
    @Value("${recaptcha.url}")
    private String recaptchaServerURL;


    @ExceptionHandler(ClassNotFoundException.class)
    @GetMapping("/register")
    public String getSignUp(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            User user = new User();
            model.addAttribute("user", user);
            return "register";
        }
        return "redirect:/";
    }

    @PostMapping("/signUp")
    public String verification(@ModelAttribute("user")  User user,
                               HttpServletRequest request, HttpServletResponse response
                                ,Model model) throws IOException {
        String gRecaptchaResponse=request.getParameter("g-recaptcha-response");
        if(!verifyReCAPTCHA(gRecaptchaResponse)) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return "/error/403";
        }
        String randomCode= RandomString.make(64);
        String url=Utility.getUrlSite(request);
        if (userService.findByEmail(user.getEmail()) == null) {


            try {
                senderEmail.sendEmailByVerificationEmail(user, url, randomCode);
            } catch (MessagingException e) {
                throw new RuntimeException(e);
            }
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            String passEncoder = encoder.encode(user.getPassword());
            user.setVerificationCode(randomCode);
            user.setEnable(false);
            user.setPassword(passEncoder);
            userService.registrationUser(user);
            return "register_success";
        }
        else {
            model.addAttribute("errorEmail","Sorry، this Gmail is registered، please go to the login page.");
            return "/register";
        }
    }
    @GetMapping("/verify")
    public String submit(@Param("code")String code) throws IOException {
        User user=userService.findUserByVerificationCode(code);
        user.setEnable(true);
        userService.registrationUser(user);
        UserDetails userDetails =new MyUserDetails(user);
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);


        return "redirect:/";
    }

    private boolean verifyReCAPTCHA(String gRecaptchaResponse) {
        HttpHeaders httpHeaders=new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String ,String > map=new LinkedMultiValueMap<>();
        map.add("secret",recaptchaSecret);
        map.add("response",gRecaptchaResponse);
        HttpEntity<MultiValueMap<String,String >> request=new HttpEntity<>(map,httpHeaders);
        RecaptchaResponse response=restTemplate.postForObject(recaptchaServerURL,request,RecaptchaResponse.class);
        System.out.println("Success: "+response.isSuccess());
        System.out.println("HostName: "+response.getHostname());
        System.out.println("Challenge: "+response.getChallenge_ts());
        if (response .getError_codes() != null){
            for (String error:response.getError_codes()){
                System.out.println("Error: "+error);
            }
        }
        return response.isSuccess();
    }




}
