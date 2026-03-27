package com.example.app.farmUser.presentation.request;

import com.example.app.farmUser.application.command.FarmUserRoleUpdateCommand;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FarmUserRoleUpdateRequest {
  private Long roleId;

  public FarmUserRoleUpdateCommand tocommand() {
    return new FarmUserRoleUpdateCommand(roleId);
  }
}
