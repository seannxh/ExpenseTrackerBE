package com.springboot.api.expensetracker.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "user_details")
public class UserModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;

    private boolean isOauthUser = false;
    @JsonIgnore
    private String password;

    public boolean isOauthUser() {
        return isOauthUser;
    }

    public UserModel(String name, String email, String password, boolean isOauthUser, Long id) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.isOauthUser = isOauthUser;
        this.id = id;
    }
    public void setIsOauthUser(boolean isOauthUser) {
        this.isOauthUser = isOauthUser;
    }

    //Empty UserModel so spring can create manage this for us.
    public UserModel() {}

    @Override
    public String toString() {
        return "UserModel{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", isOauthUser=" + isOauthUser +
                ", password='" + password + '\'' +
                '}';
    }
}
