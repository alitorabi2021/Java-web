package com.example.myproject.service;

import com.example.myproject.model.AuthenticationProvider;
import com.example.myproject.model.User;
import com.example.myproject.repository.UserRepository;;
import lombok.RequiredArgsConstructor;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.Date;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {
//    public static final int MAX_FAILED_ATTEMPTS=5;

    private final UserRepository userRepository;


    public User registrationUser(User user) {
        user.setAuthenticationProvider(AuthenticationProvider.LOCAL);
        return userRepository.save(user);
    }
    public User findUserByVerificationCode(String code){
        return userRepository.findByVerificationCode(code);
    }


    public User findByEmail(String email) {
        return userRepository.getUserByEmail(email);
    }


    public void createNewCustomerAfterOAuthLoginSuccess(String email, String name, AuthenticationProvider provider) {
        User user = new User();
        user.setEmail(email);
        user.setFirstName(name);
        user.setLastName(name);
        user.setAuthenticationProvider(provider);
        userRepository.save(user);
    }


    public void updateCustomerAfterOAuthLoginSuccess(User user, String name, AuthenticationProvider authenticationProvider) {
        user.setFirstName(name);
        user.setAuthenticationProvider(authenticationProvider);
        userRepository.save(user);
    }


    public void updateRestPassword(String token, String email) throws UserNotfoundException {
        User user=userRepository.getUserByEmail(email);
        if (user != null){
            user.setRestPasswordToken(token);
            userRepository.save(user);
        }else {
            throw new UserNotfoundException("could not find any user with email"+email);
        }
    }

//

    public User get(String restPasswordToken){
        return userRepository.findByRestPasswordToken(restPasswordToken);
    }


    public void updatePassword(User user,String newPassword,String token){
        BCryptPasswordEncoder bCryptPasswordEncoder=new BCryptPasswordEncoder();
            String passwordEncoder=bCryptPasswordEncoder.encode(newPassword);
            user.setPassword(passwordEncoder);
            user.setPasswordChangeTime(new Date());
            user.setRestPasswordToken(token);
            userRepository.save(user);
    }
    public void updatePassword(User user,String newPassword){
        BCryptPasswordEncoder bCryptPasswordEncoder=new BCryptPasswordEncoder();
        String passwordEncoder=bCryptPasswordEncoder.encode(newPassword);
        user.setPassword(passwordEncoder);
        user.setPasswordChangeTime(new Date());
        userRepository.save(user);
    }


    public void deleteUser(String email){
        User user=userRepository.getUserByEmail(email);
        userRepository.delete(user);
    }




    //    private void sendOTPEmail(User user, String otp) throws MessagingException, UnsupportedEncodingException {
    //       MimeMessage message=javaMailSender.createMimeMessage();
    //       MimeMessageHelper helper=new MimeMessageHelper(message);
    //       helper.setFrom("cantact@myProject.com","MyProject Support");
    //       helper.setTo(user.getEmail());
    //       String subject="Here's your One-Time Password (OTP) -Expire in 5 minutes!";
    //       String content="<p>Hello "+user.getFirstName() + ",</p>" +
    //               "<p> For Security reason , you're required to use the following One-Time Password to login:</p>"+
    //               "<p><b>"+otp+"</b></p>"
    //               +"<br>"
    //               +  "<p>Note: this OTP is set to expire in 5 minutes</p>";
    //       helper.setSubject(subject);
    //       helper.setText(content,true);
    //       javaMailSender.send(message);
    //
    //       System.out.println("email was send");
    //
    //   }
    //
//    @Override
//    public void generatedOneTimePassword(User user) throws MessagingException, UnsupportedEncodingException {
//        String OTP= RandomString.make(8);
//        System.out.println("OTP :"+OTP);
//        BCryptPasswordEncoder encoder=new BCryptPasswordEncoder();
//        String encoderOTP=encoder.encode(OTP);
//        user.setOneTimePassword(encoderOTP);
//        user.setOtpRequestedTime(new Date());
//        userRepository.save(user);
//        sendOTPEmail(user,OTP);
//    }

 //   public void clearOTP(User user){
//            user.setOtpRequestedTime(null);
//           user.setOneTimePassword(null);
//           userRepository.save(user);
//        System.out.println("Cleared OTP");
//    }



}