package com.example.app.auth.presentation;

import com.example.app.auth.application.command.SignUpCommand;
import com.example.app.auth.application.result.SignUpResult;
import com.example.app.auth.application.usecase.SignUpUseCase;
import com.example.app.auth.presentation.dto.request.SignUpRequest;
import com.example.app.auth.presentation.dto.response.SignUpResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
public class AuthController {
    private final SignUpUseCase signUpUseCase;

    @Operation(summary = "회원 가입")
    @PostMapping("/sign-up")
    public SignUpResponse signUp(@Valid @RequestBody SignUpRequest request) {
        SignUpCommand command = SignUpCommand.builder()
                .email(request.getEmail())
                .name(request.getName())
                .password(request.getPassword())
                .build();

        SignUpResult result = signUpUseCase.signUp(command);

        return SignUpResponse.builder()
                .accessToken(result.getAccessToken())
                .expiresIn(result.getExpiresIn())
                .expiredAt(result.getExpiredAt())
                .build();
    }
}
