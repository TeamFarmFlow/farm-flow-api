package com.example.app.auth.application.result;

import com.example.app.user.domain.enums.UserType;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class SignUpUserResult {
    private Long id;
    private String email;
    private String name;
    private UserType type;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
