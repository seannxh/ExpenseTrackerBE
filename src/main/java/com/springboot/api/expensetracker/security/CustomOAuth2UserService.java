package com.springboot.api.expensetracker.security;

import com.springboot.api.expensetracker.model.UserModel;
import com.springboot.api.expensetracker.repository.UserRepository;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    public CustomOAuth2UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        // Get email and name from Google profile
        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");

        if (email == null) {
            throw new RuntimeException("Email not found from OAuth2 provider");
        }

        // Check if user already exists in the database
        Optional<UserModel> userOptional = userRepository.findByEmail(email);

        if (userOptional.isEmpty()) {
            // If not, create a new user
            UserModel newUser = new UserModel();
            newUser.setEmail(email);
            newUser.setName(name);
            newUser.setIsOauthUser(true);
            // mark that it's an OAuth user

            // Save new user to the database
            userRepository.save(newUser);
        }

        return oAuth2User;
    }
}
