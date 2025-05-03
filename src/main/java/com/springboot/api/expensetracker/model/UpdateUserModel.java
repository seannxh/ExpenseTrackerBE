package com.springboot.api.expensetracker.model;

public class UpdateUserModel {
    private String name;
    private String password;

    // add password or email if you want to support changing those
    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setName(String name) {
        this.name = name;
    }
}
