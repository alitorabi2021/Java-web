package com.example.myproject.security;


import com.example.myproject.model.User;
import com.example.myproject.repository.UserRepository;
import com.example.myproject.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

@Service
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {


    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        log.debug("Authentication User Email"+email);
        User user= userRepository.getUserByEmail(email);
        if (user==null){

            log.error("User not found. Login  failed");
            throw new UsernameNotFoundException("Could not find user");
        }

        return new MyUserDetails(user);
    }


}
