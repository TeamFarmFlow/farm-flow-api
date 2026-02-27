package com.example.app.auth.application.command;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class SignUpCommand {
    private final String email;
    private final String name;
    private final String password;
}
