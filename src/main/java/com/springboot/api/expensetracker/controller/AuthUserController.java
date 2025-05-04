package com.springboot.api.expensetracker.controller;

import com.springboot.api.expensetracker.model.LoginModel;
import com.springboot.api.expensetracker.model.SignupModel;
import com.springboot.api.expensetracker.model.UpdateUserModel;
import com.springboot.api.expensetracker.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthUserController {

    private final AuthService authService;

    public AuthUserController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/signup")
    public Map<String, String> signup(@RequestBody SignupModel request) {
        return authService.signup(request);
    }

    @PostMapping("/login")
    public Map<String, String> login(@RequestBody LoginModel request) {
        return authService.login(request);
    }

    @PostMapping("/refresh")
    public Map<String, String> refresh(@RequestBody Map<String, String> body) {
        return authService.refreshToken(body.get("refreshToken"));
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateUser(@RequestBody UpdateUserModel request, Authentication auth) {
        authService.updateUser(request, auth.getName());
        return ResponseEntity.ok(Map.of("message", "User updated successfully"));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteUser(Authentication auth) {
        authService.deleteUser(auth.getName());
        return ResponseEntity.ok("User deleted");
    }
}



