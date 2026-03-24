package com.example.app.role.application;

import com.example.app.farm.domain.Farm;
import com.example.app.farm.domain.FarmRepository;
import com.example.app.farm.domain.FarmUserRepository;
import com.example.app.farm.domain.exception.FarmNotFoundException;
import com.example.app.role.application.command.RoleRegisterCommand;
import com.example.app.role.application.command.RoleUpdateCommand;
import com.example.app.role.domain.*;
import com.example.app.role.domain.enums.PermissionKey;
import com.example.app.role.domain.exception.*;
import com.example.app.role.presentation.response.RoleRegisterResponse;
import com.example.app.role.presentation.response.RoleUpdateResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RoleService {

  private final RoleRepository roleRepository;
  private final FarmRepository farmRepository;
  private final FarmUserRepository farmUserRepository;

  @Transactional
  public RoleRegisterResponse register(RoleRegisterCommand command, Long farmId, Long userId) {
    Farm farm =
        farmRepository.findById(farmId).orElseThrow(() -> new FarmNotFoundException(farmId));

    boolean isManageRoles =
        farmUserRepository.existsByUserIdAndPermissionKey(
            farmId, userId, PermissionKey.MANAGE_ROLES);
    if (!isManageRoles) {
      throw new RolePermissionDeniedException();
    }

    boolean existsRole = roleRepository.existsByFarm_IdAndKey(farmId, command.roleKey());
    if (existsRole) {
      throw new RoleAlreadyExistsException(command.roleKey());
    }

    Role role =
        Role.builder()
            .farm(farm)
            .key(command.roleKey())
            .name(command.name())
            .isSystem(false)
            .build();

    Role savedRole = roleRepository.save(role);

    for (PermissionKey key : command.permissionKeys()) {
      savedRole.addPermission(key);
    }

    List<PermissionKey> permissions =
        savedRole.getRolePermissions().stream()
            .map(rolePermission -> rolePermission.getId().getPermissionKey())
            .toList();

    return new RoleRegisterResponse(
        savedRole.getId(), savedRole.getName(), savedRole.getKey(), permissions);
  }

  @Transactional
  public RoleUpdateResponse update(Long id, RoleUpdateCommand command, Long farmId, Long userId) {
    boolean isManageRoles =
        farmUserRepository.existsByUserIdAndPermissionKey(
            farmId, userId, PermissionKey.MANAGE_ROLES);
    if (!isManageRoles) {
      throw new RolePermissionDeniedException();
    }

    Role role =
        roleRepository.findByIdAndFarm_Id(id, farmId).orElseThrow(RoleNotFoundException::new);
    role.update(command);
    role.clearPermissions();
    for (PermissionKey key : command.permissionKeys()) {
      role.addPermission(key);
    }

    List<PermissionKey> permissions =
        role.getRolePermissions().stream()
            .map(rolePermission -> rolePermission.getId().getPermissionKey())
            .toList();

    return new RoleUpdateResponse(role.getId(), role.getName(), role.getKey(), permissions);
  }

  @Transactional
  public void delete(Long id, Long farmId, Long userId) {
    boolean isManageRoles =
        farmUserRepository.existsByUserIdAndPermissionKey(
            farmId, userId, PermissionKey.MANAGE_ROLES);
    if (!isManageRoles) {
      throw new RolePermissionDeniedException();
    }

    Role role =
        roleRepository.findByIdAndFarm_Id(id, farmId).orElseThrow(RoleNotFoundException::new);

    if (role.isSystem()) {
      throw new SystemRoleDeleteNotAllowedException();
    }

    boolean isAssigned = farmUserRepository.existsByRole_Id(id);
    if (isAssigned) {
      throw new RoleInUseException();
    }

    roleRepository.delete(role);
  }
}
