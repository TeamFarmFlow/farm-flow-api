package com.example.app.role.application;

import com.example.app.farm.domain.Farm;
import com.example.app.role.domain.*;
import com.example.app.role.domain.enums.SystemRolePreset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RoleSeedService {
  private final RoleRepository roleRepository;
  private final RolePermissionRepository rolePermissionRepository;

  @Transactional
  public Map<String, Role> seed(Farm farm) {
    Map<String, Role> roleMap = new HashMap<>();

    for (SystemRolePreset preset : SystemRolePreset.values()) {
      Role role =
          Role.builder()
              .farm(farm)
              .key(preset.getRoleKey())
              .name(preset.getRoleName())
              .isSystem(true)
              .build();
      roleRepository.save(role);

      List<RolePermission> rolePermissions =
          preset.getPermissions().stream()
              .map(
                  p ->
                      RolePermission.builder()
                          .id(new RolePermissionId(role.getId(), p.name()))
                          .role(role)
                          .build())
              .toList();

      rolePermissionRepository.saveAll(rolePermissions);
      roleMap.put(preset.getRoleKey(), role);
    }

    return roleMap;
  }
}
