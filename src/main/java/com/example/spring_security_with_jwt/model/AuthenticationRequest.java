package com.example.spring_security_with_jwt.model;

import lombok.Getter;

@Getter
public class AuthenticationRequest {
    private String username;
    private String password;
}
