package com.example.app.auth.application.usecase;

import com.example.app.auth.application.AuthService;
import com.example.app.auth.application.command.SignUpCommand;
import com.example.app.auth.application.result.SignUpResult;
import com.example.app.auth.domain.exception.DuplicateEmailException;
import com.example.app.user.application.UserService;
import com.example.app.user.domain.User;
import com.example.app.user.domain.enums.UserType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SignUpUseCase {
    private final AuthService authService;
    private final UserService userService;

    public SignUpResult signUp(SignUpCommand command) {
        if (userService.hasUserByEmail(command.getEmail())) {
         throw new DuplicateEmailException(command.getEmail());
        }

        User user = userService.saveUser(UserType.EMPLOYER, command.getEmail(),command.getName(),  command.getPassword());
        String accessToken = authService.issueAccessToken(user);

//        TODO accessToken 만료 시간, 일시 뽑고, refreshToken 발급 후 Cookie에 박아 넣어야 함




        return SignUpResult.builder().accessToken(accessToken).build();
    }
}
