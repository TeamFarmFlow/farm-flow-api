package com.example.app.invitation.presentation.dto.request;

import com.example.app.invitation.application.command.FarmInvitationRegisterCommand;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FarmInvitationRegisterRequest {
  @Email(message = "이메일 형식이 올바르지 않습니다.")
  @NotBlank(message = "이메일은 필수 입력값입니다.")
  private String email;

  public FarmInvitationRegisterCommand toCommand() {
    return new FarmInvitationRegisterCommand(email);
  }
}
