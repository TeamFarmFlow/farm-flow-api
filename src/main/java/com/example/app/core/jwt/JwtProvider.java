package com.example.app.core.jwt;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.example.app.user.domain.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtProvider {

  private final SecretKey secretKey;
  private final long accessTokenExpireMs;

  public JwtProvider(
      @Value("${jwt.secret}") String secret,
      @Value("${jwt.access-token-expire-ms}") long accessTokenExpireMs) {
    this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    this.accessTokenExpireMs = accessTokenExpireMs;
  }

  public String createAccessToken(User user) {
    return createAccessToken(JwtClaim.userOf(user));
  }

  public String createAccessToken(JwtClaim claim) {
    Date now = new Date();
    Date exp = new Date(now.getTime() + accessTokenExpireMs);

    return Jwts.builder()
        .subject(claim.getEmail())
        .claim("id", claim.getId())
        .claim("email", claim.getEmail())
        .claim("type", claim.getType().name())
        .issuedAt(now)
        .expiration(exp)
        .signWith(secretKey)
        .compact();
  }

  public Claims parseClaims(String token) {
    return Jwts.parser()
        .verifyWith(secretKey)
        .build()
        .parseSignedClaims(token)
        .getPayload();
  }

  public boolean validate(String token) {
    try {
      parseClaims(token);
      return true;
    } catch (JwtException | IllegalArgumentException e) {
      return false;
    }
  }

  public JwtClaim getJwtClaim(String token) {
    return JwtClaim.claimsOf(parseClaims(token));
  }

  public String resolveToken(String authorizationHeader) {
    if (authorizationHeader == null) {
      return null;
    }

    if (!authorizationHeader.startsWith("Bearer ")) {
      return null;
    }

    return authorizationHeader.substring(7);
  }
}