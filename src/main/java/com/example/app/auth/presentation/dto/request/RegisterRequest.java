package com.example.app.auth.presentation.dto.request;

import com.example.app.auth.application.command.RegisterCommand;
import com.example.app.core.validation.MatchValue;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@MatchValue(first = "password", second = "confirmPassword", message = "비밀번호와 비밀번호 확인이 일치하지 않습니다.")
public class RegisterRequest {
  @Email(message = "이메일 형식이 올바르지 않습니다.")
  @NotBlank(message = "이메일은 필수 입력값입니다.")
  private String email;

  @NotBlank(message = "이름은 필수 입력값입니다.")
  private String name;

  @NotBlank(message = "비밀번호는 필수 입력값입니다.")
  private String password;

  @NotBlank(message = "비밀번호는 필수 입력값입니다.")
  private String confirmPassword;

  public RegisterCommand toCommand() {
    return new RegisterCommand(email, name, password);
  }
}
