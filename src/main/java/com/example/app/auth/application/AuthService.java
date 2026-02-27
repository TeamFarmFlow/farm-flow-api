package com.example.app.auth.application;

import org.springframework.stereotype.Service;

import com.example.app.auth.domain.RefreshToken;
import com.example.app.auth.domain.RefreshTokenRepository;
import com.example.app.core.jwt.JwtClaim;
import com.example.app.core.jwt.JwtProvider;
import com.example.app.user.domain.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
  private final JwtProvider jwtProvider;
  private final RefreshTokenRepository refreshTokenRepository;

  public String issueAccessToken(User user) {
    return jwtProvider.createAccessToken(JwtClaim.userOf(user));
  }

  public String issueRefreshToken(Long userId) {
    RefreshToken refreshToken = refreshTokenRepository.save(
        RefreshToken.builder()
            .user(User.builder().id(userId).build())
            .build());

    return refreshToken.getId();
  }
}
