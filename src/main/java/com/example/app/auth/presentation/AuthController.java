package com.example.app.auth.presentation;

import com.example.app.auth.application.LoginService;
import com.example.app.auth.application.RefreshTokenService;
import com.example.app.auth.application.SignUpService;
import com.example.app.auth.infrastructure.cookie.RefreshTokenCookieParser;
import com.example.app.auth.infrastructure.cookie.RefreshTokenCookieWriter;
import com.example.app.auth.presentation.dto.request.LoginRequest;
import com.example.app.auth.presentation.dto.request.SignUpRequest;
import com.example.app.auth.presentation.dto.response.AuthResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
  private final RefreshTokenCookieParser refreshTokenCookieParser;
  private final SignUpService signUpService;
  private final LoginService loginService;
  private final RefreshTokenService refreshTokenService;

  @Operation(summary = "회원가입")
  @PostMapping("/signup")
  public ResponseEntity<AuthResponse> signUp(
      @Valid @RequestBody SignUpRequest request, HttpServletResponse response) {
    var result = signUpService.signUp(request.toCommand());
    refreshTokenCookieWriter.set(response, result.refreshToken());
    return ResponseEntity.ok(AuthResponse.from(result));
  }

  @Operation(summary = "로그인")
  @PostMapping("/login")
  public ResponseEntity<AuthResponse> login(
      @Valid @RequestBody LoginRequest request, HttpServletResponse response) {
    var result = loginService.login(request.toCommand());
    refreshTokenCookieWriter.set(response, result.refreshToken());
    return ResponseEntity.ok(AuthResponse.from(result));
  }

  @Operation(summary = "토큰 갱신")
  @PostMapping("/refresh")
  public ResponseEntity<AuthResponse> refreshToken(
      HttpServletRequest request, HttpServletResponse response) {
    var result = refreshTokenService.refreshToken(refreshTokenCookieParser.parse(request));
    refreshTokenCookieWriter.set(response, result.refreshToken());
    return ResponseEntity.ok(AuthResponse.from(result));
  }
}
