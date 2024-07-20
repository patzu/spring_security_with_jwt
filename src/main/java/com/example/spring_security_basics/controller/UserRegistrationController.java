package com.example.spring_security_basics.controller;

import com.example.spring_security_basics.model.MyUser;
import com.example.spring_security_basics.service.MyUserDetailService;
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
