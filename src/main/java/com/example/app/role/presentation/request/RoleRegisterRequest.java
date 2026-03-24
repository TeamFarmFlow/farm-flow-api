package com.example.app.role.presentation.request;

import com.example.app.role.application.command.RoleRegisterCommand;
import com.example.app.role.domain.enums.PermissionKey;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RoleRegisterRequest {
  @NotBlank private String roleKey;

  @NotBlank private String name;

  @NotEmpty private List<PermissionKey> permissionKeys;

  public RoleRegisterCommand toCommand() {
    return new RoleRegisterCommand(roleKey, name, permissionKeys);
  }
}
