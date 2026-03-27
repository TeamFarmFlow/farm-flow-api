package com.example.app.farmUser.application;

import com.example.app.farm.domain.FarmRepository;
import com.example.app.farm.domain.enums.FarmStatus;
import com.example.app.farm.domain.exception.FarmNotFoundException;
import com.example.app.farmUser.application.command.FarmUserRoleUpdateCommand;
import com.example.app.farmUser.application.command.FarmUserStatusUpdateCommand;
import com.example.app.farmUser.domain.FarmUser;
import com.example.app.farmUser.domain.FarmUserRepository;
import com.example.app.farmUser.domain.enums.FarmUserStatus;
import com.example.app.farmUser.domain.exception.FarmUserNotFoundException;
import com.example.app.farmUser.domain.exception.MemberPermissionDeniedException;
import com.example.app.farmUser.presentation.response.FarmUserResponse;
import com.example.app.farmUser.presentation.response.FarmUserRoleUpdateResponse;
import com.example.app.farmUser.presentation.response.FarmUserStatusUpdateResponse;
import com.example.app.role.domain.Role;
import com.example.app.role.domain.RoleRepository;
import com.example.app.role.domain.enums.PermissionKey;
import com.example.app.role.domain.exception.RoleNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FarmUserService {
  private final FarmUserRepository farmUserRepository;
  private final FarmRepository farmRepository;
  private final RoleRepository roleRepository;

  public List<FarmUserResponse> getFarmUsers(Long farmId, Long userId) {
    boolean existsFarm = farmRepository.existsByIdAndStatus(farmId, FarmStatus.ACTIVE);
    if (!existsFarm) {
      throw new FarmNotFoundException(farmId);
    }

    boolean isManageMember =
        farmUserRepository.existsByFarmIdAndUserIdAndPermissionKey(
            farmId, userId, PermissionKey.MANAGE_MEMBERS);
    if (!isManageMember) {
      throw new MemberPermissionDeniedException();
    }

    return farmUserRepository.findUsersByFarmId(farmId).stream()
        .map(
            u ->
                new FarmUserResponse(
                    u.id(), u.name(), u.email(), u.status(), u.roleName(), u.roleId()))
        .toList();
  }

  @Transactional
  public FarmUserRoleUpdateResponse updateFarmUserRole(
      Long farmId, Long targetUserId, Long userId, FarmUserRoleUpdateCommand command) {
    boolean existsFarm = farmRepository.existsByIdAndStatus(farmId, FarmStatus.ACTIVE);
    if (!existsFarm) {
      throw new FarmNotFoundException(farmId);
    }

    boolean isManageMember =
        farmUserRepository.existsByFarmIdAndUserIdAndPermissionKey(
            farmId, userId, PermissionKey.MANAGE_MEMBERS);
    if (!isManageMember) {
      throw new MemberPermissionDeniedException();
    }

    FarmUser farmUser =
        farmUserRepository
            .findByFarm_IdAndUser_IdAndStatus(farmId, targetUserId, FarmUserStatus.ACTIVE)
            .orElseThrow(() -> new FarmUserNotFoundException(targetUserId));
    Long updateRoleId = command.roleId();
    Role role = roleRepository.findById(updateRoleId).orElseThrow(RoleNotFoundException::new);
    farmUser.updateRole(role);

    return farmUserRepository.findRoleUpdateInfoByFarmIdAndUserId(farmId, targetUserId);
  }

  @Transactional
  public FarmUserStatusUpdateResponse removeFarmUser(
      Long farmId, Long id, Long userId, FarmUserStatusUpdateCommand command) {
    boolean existsFarm = farmRepository.existsByIdAndStatus(farmId, FarmStatus.ACTIVE);
    if (!existsFarm) {
      throw new FarmNotFoundException(farmId);
    }

    boolean isManageMember =
        farmUserRepository.existsByFarmIdAndUserIdAndPermissionKey(
            farmId, userId, PermissionKey.MANAGE_MEMBERS);
    if (!isManageMember) {
      throw new MemberPermissionDeniedException();
    }

    FarmUser farmUser =
        farmUserRepository
            .findByFarm_IdAndUser_Id(farmId, id)
            .orElseThrow(() -> new FarmUserNotFoundException(id));
    farmUser.updateStatus(FarmUserStatus.REMOVED);

    return new FarmUserStatusUpdateResponse(id);
  }
}
