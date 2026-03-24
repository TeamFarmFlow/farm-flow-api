package com.example.app.role.presentation.request;

import com.example.app.role.application.command.RoleUpdateCommand;
import com.example.app.role.domain.enums.PermissionKey;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RoleUpdateRequest {
  @NotBlank private String name;

  @NotEmpty private List<PermissionKey> permissions;

  public RoleUpdateCommand toCommand() {
    return new RoleUpdateCommand(name, permissions);
  }
}
