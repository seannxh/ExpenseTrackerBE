package com.springboot.api.expensetracker.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LoginModel {
    private String email;
    private String password;
}
