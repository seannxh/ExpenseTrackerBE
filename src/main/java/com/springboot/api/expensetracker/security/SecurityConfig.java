package com.springboot.api.expensetracker.security;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.crypto.spec.SecretKeySpec;

@Configuration
@AllArgsConstructor
@EnableWebSecurity//Enables spring's web support with spring MVC
public class SecurityConfig {

    private final CustomOAuth2SuccessHandler successHandler;
    private final JwtUtils jwtUtils;

    //setting decoder up
    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(jwtUtils);
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        SecretKeySpec key = (SecretKeySpec) jwtUtils.getKey();
        return NimbusJwtDecoder.withSecretKey(key).build();
    }

    //Password hasher/checker
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors().and()
                .authorizeHttpRequests(auth -> auth
                        //these url are public
                        .antMatchers(
                                "/",
                                "/api/auth/**",     // login/register endpoints
                                "/swagger-ui/**",   // if using Swagger/OpenAPI
                                "/v3/api-docs/**",  // OpenAPI docs
                                "/favicon.ico",     // optional
                                "/error"            // Spring boot error fallback
                        ).permitAll()

                        //Any other request private
                        .anyRequest().authenticated()
                )
                //Oauth2 handler
                .oauth2Login(oauth2 -> oauth2.successHandler(successHandler))
                .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                //Only for backend testing purpose
                .logout(logout -> logout.logoutSuccessUrl("/"))
                .csrf(csrf -> csrf.disable());

        return http.build();
    }
}

