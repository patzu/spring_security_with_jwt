package com.example.spring_security_with_jwt.service;

import com.example.spring_security_with_jwt.entity.MyUser;
import com.example.spring_security_with_jwt.repository.MyUserRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MyUserDetailService implements UserDetailsService {
    private final MyUserRepository myUserRepository;
    private final PasswordEncoder passwordEncoder;

    public MyUserDetailService(MyUserRepository myUserRepository, PasswordEncoder passwordEncoder) {
        this.myUserRepository = myUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        Optional<MyUser> myUserOptional = myUserRepository.findByUserName(userName);
        if (myUserOptional.isPresent()) {
            MyUser myUser = myUserOptional.get();

            return User.builder()
                    .username(myUser.getUserName())
                    .password(myUser.getPassword())
                    .roles(myUser.getRoles().split(","))
                    .build();
        } else {
            throw new UsernameNotFoundException("User not found with username: "+userName);
        }
    }

    public MyUser saveUser(MyUser myUser) {
        myUser.setPassword(passwordEncoder.encode(myUser.getPassword()));
        return myUserRepository.save(myUser);
    }
}
