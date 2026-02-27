package com.example.app.core.jwt;

import java.io.IOException;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

  private final JwtProvider jwtProvider;
  private final AuthenticationEntryPoint authenticationEntryPoint;

  @Override
  protected void doFilterInternal(
      HttpServletRequest request,
      HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {

    String token = jwtProvider.resolveToken(request.getHeader(HttpHeaders.AUTHORIZATION));

    if (token == null) {
      filterChain.doFilter(request, response);
      return;
    }

    if (!jwtProvider.validate(token)) {
      SecurityContextHolder.clearContext();
      authenticationEntryPoint.commence(
          request,
          response,
          new InsufficientAuthenticationException("Invalid or expired token"));
      return;
    }

    JwtClaim claim = jwtProvider.getJwtClaim(token);
    CustomUserDetails principal = new CustomUserDetails(claim);
    Authentication authentication = new UsernamePasswordAuthenticationToken(
        principal,
        null,
        principal.getAuthorities());

    SecurityContextHolder.getContext().setAuthentication(authentication);

    filterChain.doFilter(request, response);
  }
}