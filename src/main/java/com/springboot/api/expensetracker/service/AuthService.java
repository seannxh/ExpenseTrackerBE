package com.springboot.api.expensetracker.service;

import com.springboot.api.expensetracker.model.LoginModel;
import com.springboot.api.expensetracker.model.SignupModel;
import com.springboot.api.expensetracker.model.UpdateUserModel;
import com.springboot.api.expensetracker.model.UserModel;

import java.util.Map;

public interface AuthService {
    Map<String, String> signup(SignupModel model);
    Map<String, String> login(LoginModel model);
    Map<String, String> refreshToken(String refreshToken);
    UserModel updateUser(UpdateUserModel request, String email);
    void deleteUser(String email);
    void handleOAuthLogin(String email, String name);

}
