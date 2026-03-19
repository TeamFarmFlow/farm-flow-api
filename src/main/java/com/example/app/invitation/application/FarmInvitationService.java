package com.example.app.invitation.application;

import com.example.app.farm.domain.Farm;
import com.example.app.farm.domain.FarmRepository;
import com.example.app.farm.domain.FarmUserRepository;
import com.example.app.farm.domain.exception.FarmNotFoundException;
import com.example.app.invitation.application.command.FarmInvitationAcceptCommand;
import com.example.app.invitation.application.command.FarmInvitationRegisterCommand;
import com.example.app.invitation.domain.FarmInvitation;
import com.example.app.invitation.domain.FarmInvitationRepository;
import com.example.app.invitation.domain.enums.FarmInvitationStatus;
import com.example.app.invitation.domain.exception.AlreadyFarmInvitedException;
import com.example.app.invitation.domain.exception.AlreadyFarmMemberException;
import com.example.app.invitation.domain.exception.InviterNotFoundException;
import com.example.app.role.domain.Role;
import com.example.app.role.domain.RoleRepository;
import com.example.app.role.domain.enums.SystemRolePreset;
import com.example.app.user.domain.User;
import com.example.app.user.domain.UserRepository;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import lombok.RequiredArgsConstructor;
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
        farmRepository.findById(farmId).orElseThrow(() -> new FarmNotFoundException(farmId));

    User inviter =
        farmUserRepository
            .findOwnerUser(farmId, userId, SystemRolePreset.OWNER.getRoleKey())
            .orElseThrow(() -> new InviterNotFoundException(userId));

    String inviteeEmail = command.email().trim().toLowerCase();

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

  @Transactional
  public void acceptInvitation(FarmInvitationAcceptCommand command, Long userId) {}
}
