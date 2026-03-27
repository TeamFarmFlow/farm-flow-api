package com.example.app.farmUser.presentation.request;

import com.example.app.farmUser.application.command.FarmUserStatusUpdateCommand;
import com.example.app.farmUser.domain.enums.FarmUserStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FarmUserStatusUpdateRequest {
  private FarmUserStatus status;

  public FarmUserStatusUpdateCommand toCommand() {
    return new FarmUserStatusUpdateCommand(status);
  }
}
