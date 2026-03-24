package com.example.app.role.presentation.response;

import com.example.app.role.domain.enums.PermissionKey;
import java.util.List;

public record RoleUpdateResponse(
    Long id, String name, String roleKey, List<PermissionKey> permissions) {}
