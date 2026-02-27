package com.example.app.auth.application;

import com.example.app.auth.application.command.LoginCommand;
import com.example.app.auth.application.result.AuthResult;
import com.example.app.auth.application.result.AuthUser;
import com.example.app.auth.domain.exception.WrongEmailOrPasswordException;
import com.example.app.auth.infrastructure.jwt.JwtTokenIssuer;
import com.example.app.user.application.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LoginService {
  private final JwtTokenIssuer jwtTokenIssuer;
  private final UserService userService;

  @Transactional
  public AuthResult login(LoginCommand command) {
    var user =
        userService.getUserByEmail(command.email()).orElseThrow(WrongEmailOrPasswordException::new);

    if (!userService.isMatchPassword(user, command.password())) {
      throw new WrongEmailOrPasswordException();
    }

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
