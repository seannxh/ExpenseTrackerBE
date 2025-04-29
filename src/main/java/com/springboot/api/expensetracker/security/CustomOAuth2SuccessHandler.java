package com.springboot.api.expensetracker.security;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.springboot.api.expensetracker.model.UserModel;
import org.springframework.security.oauth2.core.user.OAuth2User;
import com.springboot.api.expensetracker.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;

@Component
public class CustomOAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;

    public CustomOAuth2SuccessHandler(JwtUtils jwtUtils, UserRepository userRepository) {
        this.jwtUtils = jwtUtils;
        this.userRepository = userRepository;
    }


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");

        if (email != null) {
            Optional<UserModel> userOptional = userRepository.findByEmail(email);
            if (userOptional.isEmpty()) {
                UserModel newUser = new UserModel();
                newUser.setEmail(email);
                newUser.setName(name);
                newUser.setIsOauthUser(true);
                userRepository.save(newUser); // <<< Save new user if not exist
            }
        }

        // Generate JWT after user is saved or found
        String jwtToken = jwtUtils.generateToken(email);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String json = "{\"token\": \"" + jwtToken + "\"}";
        response.getWriter().write(json);
    }

}
