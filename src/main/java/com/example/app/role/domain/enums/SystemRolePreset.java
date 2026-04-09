package com.example.app.role.domain.enums;

import java.util.Set;
import lombok.Getter;

@Getter
public enum SystemRolePreset {
  OWNER(
      "OWNER",
      "농장주",
      Set.of(
          PermissionKey.ATTENDANCE_MANAGE,
          PermissionKey.ROOM_MANAGE,
          PermissionKey.ROOM_READING_CREATE,
          PermissionKey.ROOM_READING_UPDATE,
          PermissionKey.ROOM_READING_DELETE,
          PermissionKey.ROOM_READING_READ,
          PermissionKey.CYCLE_START,
          PermissionKey.CYCLE_THINNING,
          PermissionKey.CYCLE_HARVEST,
          PermissionKey.CYCLE_END,
          PermissionKey.CYCLE_READ,
          PermissionKey.INVITE_MEMBER,
          PermissionKey.MANAGE_MEMBERS,
          PermissionKey.MANAGE_ROLES)),
  WORKER(
      "WORKER",
      "근로자",
      Set.of(PermissionKey.ROOM_READING_READ, PermissionKey.CYCLE_READ));

  private final String roleKey;
  private final String roleName;
  private final Set<PermissionKey> permissions;

  SystemRolePreset(String roleKey, String roleName, Set<PermissionKey> permissions) {
    this.roleKey = roleKey;
    this.roleName = roleName;
    this.permissions = permissions;
  }
}
