package com.example.app.auth.application.signup;

import com.example.app.auth.application.signup.command.SignUpCommand;
import com.example.app.auth.application.signup.result.SignUpResult;
import com.example.app.auth.application.signup.result.SignUpUserResult;
import com.example.app.auth.domain.exception.DuplicateEmailException;
import com.example.app.auth.infrastructure.jwt.JwtTokenIssuer;
import com.example.app.core.jwt.IssueAccessTokenResult;
import com.example.app.user.application.UserService;
import com.example.app.user.domain.User;
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
  public SignUpResult signUp(SignUpCommand command) {
    if (Boolean.TRUE.equals(userService.hasUserByEmail(command.email()))) {
      throw new DuplicateEmailException(command.email());
    }

    User user =
        userService.saveUser(
            UserType.EMPLOYER, command.email(), command.name(), command.password());

    IssueAccessTokenResult issueAccessTokenResult = jwtTokenIssuer.issueAccessToken(user);
    SignUpUserResult signUpUserResult = SignUpUserResult.from(user);

    String refreshToken = jwtTokenIssuer.issueRefreshToken(user.getId());

    return new SignUpResult(
        refreshToken,
        issueAccessTokenResult.accessToken(),
        issueAccessTokenResult.expiresAt(),
        issueAccessTokenResult.expiresIn(),
        signUpUserResult);
  }
}
