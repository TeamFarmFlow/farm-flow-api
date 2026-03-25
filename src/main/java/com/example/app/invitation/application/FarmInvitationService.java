package com.example.app.invitation.application;

import com.example.app.farm.domain.*;
import com.example.app.farm.domain.enums.FarmStatus;
import com.example.app.farm.domain.exception.FarmNotFoundException;
import com.example.app.farmUser.domain.FarmUser;
import com.example.app.farmUser.domain.FarmUserId;
import com.example.app.farmUser.domain.FarmUserRepository;
import com.example.app.farmUser.domain.enums.FarmUserStatus;
import com.example.app.invitation.application.command.FarmInvitationAcceptCommand;
import com.example.app.invitation.application.command.FarmInvitationRegisterCommand;
import com.example.app.invitation.domain.FarmInvitation;
import com.example.app.invitation.domain.FarmInvitationRepository;
import com.example.app.invitation.domain.enums.FarmInvitationStatus;
import com.example.app.invitation.domain.exception.*;
import com.example.app.invitation.presentation.dto.response.FarmInvitationResponse;
import com.example.app.role.domain.Role;
import com.example.app.role.domain.RoleRepository;
import com.example.app.role.domain.enums.SystemRolePreset;
import com.example.app.user.domain.User;
import com.example.app.user.domain.UserRepository;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FarmInvitationService {

  private final FarmRepository farmRepository;
  private final FarmUserRepository farmUserRepository;
  private final FarmInvitationRepository farmInvitationRepository;
  private final InvitationCodeService invitationCodeService;
  private final RoleRepository roleRepository;
  private final InvitationMailService invitationMailService;
  private final UserRepository userRepository;

  @Transactional
  public void createInvitation(FarmInvitationRegisterCommand command, Long farmId, Long userId) {
    Farm farm =
        farmRepository
            .findByIdAndStatus(farmId, FarmStatus.ACTIVE)
            .orElseThrow(() -> new FarmNotFoundException(farmId));

    User inviter =
        farmUserRepository
            .findOwnerUser(farmId, userId, SystemRolePreset.OWNER.getRoleKey())
            .orElseThrow(() -> new InviterNotFoundException(userId));

    String inviteeEmail = command.email().trim().toLowerCase();

    validAlreadyInvitated(farmId, inviteeEmail);

    Role role = roleRepository.findByFarmIdAndRoleKey(farmId, SystemRolePreset.WORKER.getRoleKey());
    InvitationCodeService.InvitationCode code = invitationCodeService.generate();
    Instant expiresAt = Instant.now().plus(30, ChronoUnit.MINUTES);

    FarmInvitation invitation =
        FarmInvitation.builder()
            .farm(farm)
            .inviter(inviter)
            .inviteeEmail(inviteeEmail)
            .assignedRole(role)
            .status(FarmInvitationStatus.INVITED)
            .inviteCodeHash(code.codeHash())
            .expiresAt(expiresAt)
            .build();

    farmInvitationRepository.save(invitation);
    invitationMailService.sendInvitationCode(inviteeEmail, code.rawCode());
  }

  private void validAlreadyInvitated(Long farmId, String inviteeEmail) {
    boolean alreadyMember = farmUserRepository.existsByFarm_IdAndUser_Email(farmId, inviteeEmail);
    if (alreadyMember) {
      throw new AlreadyFarmMemberException(inviteeEmail);
    }

    Boolean isInvited =
        farmInvitationRepository.existsByFarmIdAndInviteeEmailAndStatus(
            farmId, inviteeEmail, FarmInvitationStatus.INVITED);
    if (isInvited) {
      throw new AlreadyFarmInvitedException(inviteeEmail);
    }
  }

  @Transactional
  public void acceptInvitation(FarmInvitationAcceptCommand command, Long userId) {
    User user =
        userRepository.findById(userId).orElseThrow(() -> new FarmNotFoundException(userId));

    String codeHash = invitationCodeService.hash(command.code());
    FarmInvitation invitation =
        farmInvitationRepository
            .findByInviteCodeHash(codeHash)
            .orElseThrow(() -> new InvitationNotFoundException(command.code()));

    Farm farm = validateAcceptableInvitation(invitation, user, userId);

    Role role =
        roleRepository.findByFarmIdAndRoleKey(farm.getId(), SystemRolePreset.WORKER.getRoleKey());

    FarmUser farmUser =
        FarmUser.builder()
            .id(new FarmUserId(farm.getId(), userId))
            .farm(farm)
            .user(user)
            .role(role)
            .status(FarmUserStatus.ACTIVE)
            .build();
    farmUserRepository.save(farmUser);

    invitation.update(FarmInvitationStatus.ACCEPTED, user);
  }

  private Farm validateAcceptableInvitation(FarmInvitation invitation, User user, Long userId) {
    if (invitation.getStatus() != FarmInvitationStatus.INVITED) {
      throw new InvalidInvitationStatusException(invitation.getStatus());
    }

    if (invitation.getExpiresAt().isBefore(Instant.now())) {
      throw new InvalidInvitationStatusException(invitation.getStatus());
    }

    String inviteeEmail = invitation.getInviteeEmail();
    if (!inviteeEmail.equals(user.getEmail())) {
      throw new InvitationEmailMismatchException();
    }

    Long farmId = invitation.getFarm().getId();
    Farm farm =
        farmRepository
            .findByIdAndStatus(farmId, FarmStatus.ACTIVE)
            .orElseThrow(() -> new FarmNotFoundException(farmId));

    boolean alreadyMember = farmUserRepository.existsByFarm_IdAndUser_Id(farmId, userId);
    if (alreadyMember) {
      throw new AlreadyFarmMemberException();
    }

    return farm;
  }

  public Page<FarmInvitationResponse> getFarmInvitations(
      Long farmId, Long userId, Pageable pageable) {
    farmRepository
        .findByIdAndUserId(farmId, userId)
        .orElseThrow(() -> new FarmNotFoundException(farmId));

    return farmInvitationRepository
        .findByFarm_Id(farmId, pageable)
        .map(
            i ->
                new FarmInvitationResponse(
                    i.getId(),
                    i.getInviteeEmail(),
                    i.getAssignedRole().getName(),
                    i.getStatus(),
                    i.getRespondedAt(),
                    i.getInviter().getId(),
                    i.getInviter().getName(),
                    i.getInviteeUser() != null ? i.getInviteeUser().getId() : null,
                    i.getInviteeUser() != null ? i.getInviteeUser().getName() : null,
                    i.getCreatedAt(),
                    i.getUpdatedAt()));
  }

  @Transactional
  public void cancleInvitation(Long id) {
    FarmInvitation invitation =
        farmInvitationRepository.findById(id).orElseThrow(InvitationNotFoundException::new);
    invitation.cancle(FarmInvitationStatus.CANCELED);
  }
}
