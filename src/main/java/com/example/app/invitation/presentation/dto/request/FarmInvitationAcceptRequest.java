package com.example.app.invitation.presentation.dto.request;

import com.example.app.invitation.application.command.FarmInvitationAcceptCommand;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FarmInvitationAcceptRequest {
  @NotBlank private String code;

  public FarmInvitationAcceptCommand toCommand() {
    return new FarmInvitationAcceptCommand(code);
  }
}
