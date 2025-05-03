package com.springboot.api.expensetracker.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SignupModel {
    private String email;
    private String name;
    private String password;

}
