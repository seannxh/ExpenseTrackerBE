package com.springboot.api.expensetracker.controller;

import com.springboot.api.expensetracker.model.UserModel;
import com.springboot.api.expensetracker.repository.UserRepository;
import com.springboot.api.expensetracker.security.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthUserController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    @Autowired
    public AuthUserController(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtils jwtUtils) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("/signup")
    public String signup(@RequestBody SignupModel request) {
        Optional<UserModel> existingUser = userRepository.findByEmail(request.getEmail());

        if (existingUser.isPresent()) {
            return "Error: Email is already registered!";
        }

        UserModel newUser = new UserModel();
        newUser.setEmail(request.getEmail());
        newUser.setName(request.getName());
        newUser.setPassword(passwordEncoder.encode(request.getPassword())); // hash password
        userRepository.save(newUser);

        String token = jwtUtils.generateToken(newUser.getEmail());
        return "{\"token\": \"" + token + "\"}";
    }

    @GetMapping("/login")
    public String login(@RequestBody LoginModel request) {
        Optional<UserModel> userOptional = userRepository.findByEmail(request.getEmail());

        if (userOptional.isEmpty()) {
            return "Error: Invalid email or password!";
        }

        UserModel user = userOptional.get();

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return "Error: Invalid email or password!";
        }

        String token = jwtUtils.generateToken(user.getEmail());
        return "{\"token\": \"" + token + "\"}";
    }
}







