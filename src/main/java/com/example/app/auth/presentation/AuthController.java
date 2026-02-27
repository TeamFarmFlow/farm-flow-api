package com.example.app.auth.presentation;

import com.example.app.auth.application.signup.SignUpService;
import com.example.app.auth.application.signup.result.SignUpResult;
import com.example.app.auth.presentation.cookie.RefreshTokenCookieWriter;
import com.example.app.auth.presentation.dto.request.SignUpRequest;
import com.example.app.auth.presentation.dto.response.SignUpResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "인증")
@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
public class AuthController {
  private final RefreshTokenCookieWriter refreshTokenCookieWriter;
  private final SignUpService signUpService;

  @Operation(summary = "회원가입")
  @PostMapping("/signup")
  public SignUpResponse signUp(
      @Valid @RequestBody SignUpRequest request, HttpServletResponse response) {
    SignUpResult result = signUpService.signUp(request.toCommand());
    refreshTokenCookieWriter.set(response, result.refreshToken());
    return SignUpResponse.from(result);
  }
}
