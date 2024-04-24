package com.example.myproject.model;


import lombok.*;

import javax.persistence.*;
import java.util.*;

@Entity
@Setter
@Getter
@Data
@NoArgsConstructor
@Table(name = "t_user")
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    @Column(name = "rest_password_token")
    private String restPasswordToken;
    @Column(name = "password_change_time")
    private Date passwordChangeTime;
    @Column(length = 64, name = "verification_code")
    private String verificationCode;

    private boolean isEnable;
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id")
            , inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();
    @Enumerated(EnumType.STRING)
    @Column(name = "auth_provider")
    private AuthenticationProvider authenticationProvider;

    public User(String firstName, String lastName, String email, String password, Set<Role> roles) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.roles = roles;
    }

    public boolean isEnable() {
        return isEnable;
    }

    public boolean isPasswordExpired(){
        if (this.passwordChangeTime == null)
            return false;
        long currentTime=System.currentTimeMillis();
        long lastPasswordChangeTime=this.passwordChangeTime.getTime();
        return currentTime> lastPasswordChangeTime+PASSWORD_EXPIRED_TIME;
    }
    private final static long PASSWORD_EXPIRED_TIME= 60L * 24L * 60L * 60L * 1000L ;

    public String getVerificationCode() {
        return verificationCode;
    }
}





//    @Column(name = "one_time_password")
//    private String oneTimePassword;
//    @Column(name = "otp_requested_time")
//    private Date otpRequestedTime;
//    public boolean isOTPRequired(){
//        if (this.oneTimePassword == null){
//            return false;
//        }
//        long otpRequested=this.otpRequestedTime.getTime();
//
//        if (otpRequested + OTP_VALID_DURATION < System.currentTimeMillis()){
//            System.out.println("time is: "+System.currentTimeMillis());
//            return false;
//        }
//        return true;
//    }
//
//    private static final long OTP_VALID_DURATION=5 * 60 * 1000; //5 minutes
//
//}
