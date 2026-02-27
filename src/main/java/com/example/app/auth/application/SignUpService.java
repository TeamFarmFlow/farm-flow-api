package com.example.app.auth.application;

import com.example.app.auth.application.command.SignUpCommand;
import com.example.app.auth.application.result.AuthResult;
import com.example.app.auth.application.result.AuthUser;
import com.example.app.auth.domain.exception.DuplicateEmailException;
import com.example.app.auth.infrastructure.jwt.JwtTokenIssuer;
import com.example.app.user.application.UserService;
import com.example.app.user.domain.enums.UserType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SignUpService {
  private final JwtTokenIssuer jwtTokenIssuer;
  private final UserService userService;

  @Transactional
  public AuthResult signUp(SignUpCommand command) {
    if (Boolean.TRUE.equals(userService.hasUserByEmail(command.email()))) {
      throw new DuplicateEmailException(command.email());
    }

    var user =
        userService.saveUser(
            UserType.EMPLOYER, command.email(), command.name(), command.password());
    var issueAccessTokenResult = jwtTokenIssuer.issueAccessToken(user);
    var refreshToken = jwtTokenIssuer.issueRefreshToken(user.getId());

    return new AuthResult(
        refreshToken,
        issueAccessTokenResult.accessToken(),
        issueAccessTokenResult.expiresAt(),
        issueAccessTokenResult.expiresIn(),
        AuthUser.from(user));
  }
}
