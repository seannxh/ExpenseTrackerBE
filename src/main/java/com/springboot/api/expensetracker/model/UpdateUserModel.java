package com.springboot.api.expensetracker.model;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.SecondaryTable;

@Getter
@Setter
public class UpdateUserModel {
    private String name;
    private String password;


}
