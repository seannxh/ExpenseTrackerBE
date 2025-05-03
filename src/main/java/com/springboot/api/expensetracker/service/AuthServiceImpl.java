package com.springboot.api.expensetracker.service;

import com.springboot.api.expensetracker.model.LoginModel;
import com.springboot.api.expensetracker.model.SignupModel;
import com.springboot.api.expensetracker.model.UpdateUserModel;
import com.springboot.api.expensetracker.model.UserModel;
import com.springboot.api.expensetracker.repository.UserRepository;
import com.springboot.api.expensetracker.security.JwtUtils;
import io.jsonwebtoken.Jwts;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    public AuthServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtils jwtUtils) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
    }

    @Override
    public Map<String, String> signup(SignupModel request) {
        validateSignupInput(request.getName(), request.getEmail(), request.getPassword());
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            return Map.of("error", "Email is already registered!");
        }

        UserModel newUser = new UserModel();
        newUser.setEmail(request.getEmail());
        newUser.setName(request.getName());
        //Hashes passwords
        newUser.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepository.save(newUser);

        return Map.of(
                "accessToken", jwtUtils.generateToken(newUser.getEmail()),
                "refreshToken", jwtUtils.generateRefreshToken(newUser.getEmail())
        );
    }

    @Override
    public Map<String, String> login(LoginModel request) {
        //Get users by email
        UserModel user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return Map.of("error", "Invalid email or password!");
        }

        return Map.of(
                "accessToken", jwtUtils.generateToken(user.getEmail()),
                "refreshToken", jwtUtils.generateRefreshToken(user.getEmail())
        );
    }

    @Override
    public Map<String, String> refreshToken(String refreshToken) {
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

    @Override
    //Gets UpdateUserModel injects it
    //Finds the user by usermodel in user repository by email
    // if user enter a field that doesnt equal to past or null
    //it updates the name
    public UserModel updateUser(UpdateUserModel request, String email) {
        validateSignupInput(request.getName(), request.getEmail(), request.getPassword());
        UserModel user = userRepository.findByEmail(email).orElseThrow();

        if (request.getName() != null) {
            user.setName(request.getName());
        }

        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        return userRepository.save(user);
    }

    @Override
    public void deleteUser(String email) {
        UserModel user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        userRepository.delete(user);
    }

    @Override
    public void handleOAuthLogin(String email, String name) {
        //If user is not in DB save it
        if (userRepository.findByEmail(email).isEmpty()) {
            UserModel user = new UserModel();
            user.setEmail(email);
            user.setName(name);
            user.setIsOauthUser(true);
            userRepository.save(user);
        }
    }
    private void validateSignupInput(String name, String email, String password) {
        if (name == null || name.trim().isEmpty()) {
            throw new RuntimeException("Name cannot be empty");
        }

        if (name.length() < 3) {
            throw new RuntimeException("Name has to be least 3 characters long");
        }

        if (email == null || !email.matches("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            throw new RuntimeException("Invalid email format");
        }

        if (password == null || password.length() < 6 ||
                !password.matches(".*\\d.*") || // Must contain a digit
                !password.matches(".*[A-Z].*")) { // Must contain an uppercase letter
            throw new RuntimeException("Password must be at least 6 characters and contain a number and an uppercase letter");
        }
    }
}

