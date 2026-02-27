package com.example.app.auth.infrastructure.jwt;

import org.springframework.stereotype.Service;

import com.example.app.auth.domain.RefreshToken;
import com.example.app.auth.domain.RefreshTokenRepository;
import com.example.app.core.jwt.IssueAccessTokenResult;
import com.example.app.core.jwt.JwtClaim;
import com.example.app.core.jwt.JwtProvider;
import com.example.app.user.domain.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JwtTokenIssuer {
  private final JwtProvider jwtProvider;
  private final RefreshTokenRepository refreshTokenRepository;

  public IssueAccessTokenResult issueAccessToken(User user) {
    return jwtProvider.createAccessToken(JwtClaim.userOf(user));
  }

  public String issueRefreshToken(Long userId) {
    return refreshTokenRepository.save(RefreshToken.issue(userId)).getId();
  }
}
