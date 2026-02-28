package com.example.app.core.jwt;

import com.example.app.user.domain.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtProvider {

  private final SecretKey secretKey;
  private final long accessTokenExpireMs;

  public JwtProvider(
      @Value("${jwt.secret}") String secret,
      @Value("${jwt.expiration-seconds}") long accessTokenExpireMs) {
    this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    this.accessTokenExpireMs = accessTokenExpireMs;
  }

  public IssueAccessTokenResult createAccessToken(User user) {
    return createAccessToken(JwtClaim.userOf(user));
  }

  public IssueAccessTokenResult createAccessToken(JwtClaim claim) {
    Date now = new Date();
    Date expiration = new Date(now.getTime() + accessTokenExpireMs);

    String token = Jwts.builder()
        .subject(claim.getEmail())
        .claim("id", claim.getId())
        .claim("email", claim.getEmail())
        .claim("type", claim.getType().name())
        .issuedAt(now)
        .expiration(expiration)
        .signWith(secretKey)
        .compact();

    return IssueAccessTokenResult.from(token, expiration);
  }

  public Claims parseClaims(String token) {
    return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload();
  }

  public JwtClaim validate(String token) {
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
