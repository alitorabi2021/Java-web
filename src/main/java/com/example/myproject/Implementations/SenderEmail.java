package com.example.myproject.Implementations;

import com.example.myproject.model.User;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;

@Component
public class SenderEmail {
    private final JavaMailSender javaMailSender;

    public SenderEmail(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void sendEmailByForgetPassword(String email, String restPasswordLink) throws MessagingException, UnsupportedEncodingException {
        MimeMessage message=javaMailSender.createMimeMessage();
        MimeMessageHelper helper=new MimeMessageHelper(message);
        helper.setFrom("contact@8080.com","MyProject Support");
        helper.setTo(email);
        String subject="Here's the link to reset your password";
        String content="<h1><p>hello</p></h1>" +
                "<h1><p>you have requested to reset your password</p></h1>" +
                "<h1><p>Click the link below to change your password:</p></h1>" +
                "<h1><p><a href=\"" +restPasswordLink +"\">Change my password</a></p></h1>" +
                "<h1><p>Ignore this email if you fo remember your password, or you have not made the request.</p></h1>";
        helper.setSubject(subject);
        helper.setText(content,true);
        javaMailSender.send(message);
    }

    public void sendEmailByVerificationEmail(User user, String siteURL, String randomCode) throws MessagingException, UnsupportedEncodingException {
        String subject="Please verify your registration";
        String senderName="My Project Team";
        String mailContent="<p> Dear "+user.getFirstName()+user.getLastName()+",</p>";
        mailContent+="<p> Please click the link below to verify to your registration: </p>";
        String verifyURL=siteURL+ "/verify?code="+randomCode;
        mailContent+="<h3><a href=\""+verifyURL+"\">VERIFY</a></h3>";
        mailContent+="<p> Thank you <br> The My Project Team </p>";

        MimeMessage message=javaMailSender.createMimeMessage();
        MimeMessageHelper helper=new MimeMessageHelper(message);
        helper.setFrom("set your email",senderName);
        helper.setTo(user.getEmail());
        helper.setSubject(subject);
        helper.setText(mailContent,true);
        javaMailSender.send(message);
    }

}
