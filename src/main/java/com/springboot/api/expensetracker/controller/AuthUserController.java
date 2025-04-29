package com.springboot.api.expensetracker.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthUserController {

    @GetMapping("/api/protected")
    public String protectedEndpoint(Authentication authentication) {
        String email = authentication.getName();
        return "Hello, " + email + "! You have accessed a protected endpoint.";
    }




}
