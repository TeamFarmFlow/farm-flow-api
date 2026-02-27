package com.example.app.core.configuration;

import java.util.List;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.example.app.core.jwt.JwtFilter;
import com.example.app.core.jwt.JwtProvider;
import com.example.app.core.security.SecurityAuthenticationEntryPoint;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableConfigurationProperties(CorsProperties.class)
@RequiredArgsConstructor
public class SecurityConfiguration {
  private final SecurityAuthenticationEntryPoint authenticationEntryPoint;
  private final CorsProperties corsProperties;
  private final JwtProvider jwtProvider;

  @Bean
  SecurityFilterChain filterChain(HttpSecurity http) {
    return http
        .csrf(csrf -> csrf.disable())
        .cors(cors -> {
        })
        .sessionManagement(
            sessionMenagement -> sessionMenagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .exceptionHandling(eh -> eh.authenticationEntryPoint(authenticationEntryPoint))
        .authorizeHttpRequests(
            auth -> auth
                .requestMatchers("/api/v1", "/api/v1/", "/api/v1/auth/**", "/api/v1/users/signUp").permitAll()
                .requestMatchers("/api/v1/**").authenticated()
                .anyRequest().permitAll())
        .addFilterBefore(new JwtFilter(jwtProvider, authenticationEntryPoint),
            UsernamePasswordAuthenticationFilter.class)
        .build();
  }

  @Bean
  PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration config = new CorsConfiguration();

    config.setAllowedOriginPatterns(corsProperties.getAllowedOriginPatterns());
    config.setAllowedMethods(List.of("*"));
    config.setAllowedHeaders(List.of("*"));
    config.setAllowCredentials(true);

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", config);

    return source;
  }
}