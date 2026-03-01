package com.example.app.core.jwt;

import com.example.app.core.security.SecurityAuthenticationEntryPoint;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
  public final String[] PERMIT_ALL_PATHS = {"/api/v1", "/api/v1/", "/api/v1/auth/**"};

  private final JwtProvider jwtProvider;
  private final SecurityAuthenticationEntryPoint authenticationEntryPoint;
  private final AntPathMatcher matcher = new AntPathMatcher();

  private boolean isPermitAllPath(HttpServletRequest request) {
    String path = request.getServletPath();

    for (var permitAllPath : PERMIT_ALL_PATHS) {
      if (matcher.match(permitAllPath, path)) {
        return true;
      }
    }

    return false;
  }

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {

    boolean permitAll = isPermitAllPath(request);
    String token = jwtProvider.resolveToken(request.getHeader(HttpHeaders.AUTHORIZATION));

    if (token == null) {
      filterChain.doFilter(request, response);
      return;
    }

    try {
      JwtClaim claim = jwtProvider.validate(token);

      CustomUserDetails principal = new CustomUserDetails(claim);
      Authentication authentication =
          new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities());

      SecurityContextHolder.getContext().setAuthentication(authentication);
      filterChain.doFilter(request, response);

    } catch (ExpiredJwtException e) {
      SecurityContextHolder.clearContext();

      if (permitAll) {
        filterChain.doFilter(request, response);
        return;
      }

      authenticationEntryPoint.commence(
          request, response, new InsufficientAuthenticationException("Expired token", e));

    } catch (JwtException | IllegalArgumentException e) {
      SecurityContextHolder.clearContext();

      if (permitAll) {
        filterChain.doFilter(request, response);
        return;
      }

      authenticationEntryPoint.commence(
          request, response, new InsufficientAuthenticationException("Invalid token", e));
    }
  }
}
