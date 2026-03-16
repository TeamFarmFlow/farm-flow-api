package com.example.app.invitation.application;

import com.example.app.farm.domain.Farm;
import com.example.app.farm.domain.FarmRepository;
import com.example.app.farm.domain.FarmUserRepository;
import com.example.app.farm.domain.exception.FarmNotFoundException;
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
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FarmInvitationService {

  private final FarmRepository farmRepository;
  private final FarmUserRepository farmUserRepository;
  private final FarmInvitationRepository farmInvitationRepository;
  private final InvitationCodeService invitationCodeService;
  private final RoleRepository roleRepository;

  @Transactional
  public void createInvitation(FarmInvitationRegisterCommand command, Long farmId, Long userId) {
    // 존재하는 농장인지
    Farm farm =
        farmRepository.findById(farmId).orElseThrow(() -> new FarmNotFoundException(farmId));

    // 농장주가 맞는지
    User inviter =
        farmUserRepository
            .findOwnerUser(farmId, userId, SystemRolePreset.OWNER.getRoleKey())
            .orElseThrow(() -> new InviterNotFoundException(userId));

    String inviteeEmail = command.email().trim().toLowerCase();

    // 해당 이메일 멤버가 이미 멤버인건지 확인
    boolean alreadyMember = farmUserRepository.existsByFarm_IdAndUser_Email(farmId, inviteeEmail);

    if (alreadyMember) {
      throw new AlreadyFarmMemberException(inviteeEmail);
    }

    // 해당 이메일 멤버가 이미 초대되었는지 확인
    Boolean isInvited =
        farmInvitationRepository.existsByFarmIdAndInviteeEmailAndStatus(
            farmId, inviteeEmail, FarmInvitationStatus.INVITED);
    if (isInvited) {
      throw new AlreadyFarmInvitedException(inviteeEmail);
    }

    // role 조회
    Role role = roleRepository.findByFarmIdAndRoleKey(farmId, SystemRolePreset.WORKER.getRoleKey());

    // 토큰 발행
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

    FarmInvitation saved = farmInvitationRepository.save(invitation);

    // todo 메일 발송
  }
}
