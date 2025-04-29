package com.springboot.api.expensetracker.controller;

import com.springboot.api.expensetracker.model.UserModel;
import com.springboot.api.expensetracker.repository.UserRepository;
import com.springboot.api.expensetracker.security.JwtUtils;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
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
    public Map<String, String> signup(@RequestBody SignupModel request) {
        Optional<UserModel> existingUser = userRepository.findByEmail(request.getEmail());

        if (existingUser.isPresent()) {
            return Map.of("error", "Email is already registered!");
        }

        UserModel newUser = new UserModel();
        newUser.setEmail(request.getEmail());
        newUser.setName(request.getName());
        newUser.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepository.save(newUser);

        return Map.of(
                "accessToken", jwtUtils.generateToken(newUser.getEmail()),
                "refreshToken", jwtUtils.generateRefreshToken(newUser.getEmail())
        );
    }

    @PostMapping("/login")
    public Map<String, String> login(@RequestBody LoginModel request) {
        Optional<UserModel> userOptional = userRepository.findByEmail(request.getEmail());

        if (userOptional.isEmpty() || !passwordEncoder.matches(request.getPassword(), userOptional.get().getPassword())) {
            return Map.of("error", "Invalid email or password!");
        }

        UserModel user = userOptional.get();

        return Map.of(
                "accessToken", jwtUtils.generateToken(user.getEmail()),
                "refreshToken", jwtUtils.generateRefreshToken(user.getEmail())
        );
    }
    @PostMapping("/refresh")
    public Map<String, String> refreshToken(@RequestBody Map<String, String> body) {
        String refreshToken = body.get("refreshToken");

        try {
            String email = Jwts.parserBuilder()
                    .setSigningKey(jwtUtils.getKey())
                    .build()
                    .parseClaimsJws(refreshToken)
                    .getBody()
                    .getSubject();

            return Map.of("accessToken", jwtUtils.generateToken(email));
        } catch (Exception e) {
            return Map.of("error", "Invalid or expired refresh token");
        }
    }
}


