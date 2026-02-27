package com.example.app.auth.application;

import com.example.app.auth.application.result.AuthResult;
import com.example.app.auth.application.result.AuthUser;
import com.example.app.auth.domain.exception.InvalidTokenException;
import com.example.app.auth.infrastructure.jwt.JwtTokenIssuer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
  private final JwtTokenIssuer jwtTokenIssuer;

  @Transactional
  public AuthResult refreshToken(String refreshTokenValue) {
    if (refreshTokenValue == null) {
      throw new InvalidTokenException();
    }

    var refreshToken =
        jwtTokenIssuer.getRefreshToken(refreshTokenValue).orElseThrow(InvalidTokenException::new);
    var user = refreshToken.getUser();

    jwtTokenIssuer.revokeRefreshToken(refreshToken.getId());

    var issueAccessTokenResult = jwtTokenIssuer.issueAccessToken(user);
    var newRefreshToken = jwtTokenIssuer.issueRefreshToken(user.getId());

    return new AuthResult(
        newRefreshToken,
        issueAccessTokenResult.accessToken(),
        issueAccessTokenResult.expiresAt(),
        issueAccessTokenResult.expiresIn(),
        AuthUser.from(user));
  }
}
