package com.springboot.api.expensetracker.security;

import com.springboot.api.expensetracker.service.AuthService;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CustomOAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final JwtUtils jwtUtils;
    private final AuthService authService;
    //Lazy is initialized there so it doesn't get init when it's not needed.
    public CustomOAuth2SuccessHandler(JwtUtils jwtUtils, @Lazy AuthService authService) {
        this.jwtUtils = jwtUtils;
        this.authService = authService;
    }

    //Returns the JWT token in body JSON
    //On OAuth2 Authentication success
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");

        authService.handleOAuthLogin(email, name);
        String jwtToken = jwtUtils.generateToken(email);
        String refreshToken = jwtUtils.generateRefreshToken(email);


        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write("{\"token\": \"" + jwtToken + "\", \"refreshToken\": \"" + refreshToken + "\"}");
    }
}
