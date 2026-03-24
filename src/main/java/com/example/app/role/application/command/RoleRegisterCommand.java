package com.example.app.role.application.command;

import com.example.app.role.domain.enums.PermissionKey;
import java.util.List;

public record RoleRegisterCommand(
    String roleKey, String name, List<PermissionKey> permissionKeys) {}
