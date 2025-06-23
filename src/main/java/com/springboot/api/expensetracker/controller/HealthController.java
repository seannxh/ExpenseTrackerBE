package com.springboot.api.expensetracker.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {

    @GetMapping("/")
    public String Home() {
        return "Welcome to Official Backend for ExpenseTracker";
    }
}