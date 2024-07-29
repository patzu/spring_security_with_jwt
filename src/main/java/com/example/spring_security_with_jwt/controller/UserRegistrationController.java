package com.example.spring_security_with_jwt.controller;

import com.example.spring_security_with_jwt.entity.MyUser;
import com.example.spring_security_with_jwt.service.MyUserDetailService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserRegistrationController {
    private final MyUserDetailService myUserDetailService;

    public UserRegistrationController(MyUserDetailService myUserDetailService) {
        this.myUserDetailService = myUserDetailService;
    }

    @PostMapping("/register")
    public MyUser registerUser(@RequestBody MyUser myUser){
        return myUserDetailService.saveUser(myUser);
    }
}
