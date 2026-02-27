package com.example.app.core.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@RequiredArgsConstructor
public class SecurityConfiguration {
  private final SecurityAuthenticationEntryPoint authenticationEntryPoint;

  @Bean
  SecurityFilterChain filterChain(HttpSecurity http) {
    return http
        .csrf(csrf -> csrf.disable())
        .sessionManagement(
            sessionMenagement -> sessionMenagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .exceptionHandling(eh -> eh.authenticationEntryPoint(authenticationEntryPoint))
        .authorizeHttpRequests(
            auth -> auth
                .requestMatchers("/api/v1", "/api/v1/", "/api/v1/auth/**").permitAll()
                .requestMatchers("/api/v1/**").authenticated()
                .anyRequest().permitAll())
        .build();
  }

  @Bean
  PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}