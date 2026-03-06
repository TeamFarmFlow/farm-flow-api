package com.example.app.invitation.application;

import com.example.app.farm.domain.Farm;
import com.example.app.farm.domain.FarmRepository;
import com.example.app.farm.domain.FarmUserRepository;
import com.example.app.farm.domain.exception.FarmNotFoundException;
import com.example.app.invitation.application.command.FarmInvitationRegisterCommand;
import com.example.app.invitation.domain.FarmInvitationRepository;
import com.example.app.invitation.domain.exception.AlreadyFarmInvitedException;
import com.example.app.invitation.domain.exception.AlreadyFarmMemberException;
import com.example.app.invitation.domain.exception.InviterNotFoundException;
import com.example.app.role.domain.enums.SystemRolePreset;
import com.example.app.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FarmInvitationService {

  private final FarmRepository farmRepository;
  private final FarmUserRepository farmUserRepository;
  private final FarmInvitationRepository farmInvitationRepository;
  private final InvitationTokenService invitationTokenService;

  public void createInvitation(FarmInvitationRegisterCommand command, Long farmId, Long userId) {
    // 존재하는 농장인지
    Farm farm =
        farmRepository
            .findByUserId(farmId, userId)
            .orElseThrow(() -> new FarmNotFoundException(farmId));

    // 농장주가 맞는지
    User inviter =
        farmUserRepository
            .findOwnerUser(farmId, userId, SystemRolePreset.OWNER.getRoleKey())
            .orElseThrow(() -> new InviterNotFoundException(userId));

    String inviteeEmail = command.email();
    // 해당 이메일 멤버가 이미 멤버인건지 확인
    Boolean isMember = farmUserRepository.existsMemberByUserIdAndFarmId(farmId, inviteeEmail);
    if (isMember) {
      throw new AlreadyFarmMemberException(inviteeEmail);
    }

    // 해당 이메일 멤버가 이미 초대되었는지 확인
    Boolean isInvited = farmInvitationRepository.existsByEmail(inviteeEmail);
    if (isInvited) {
      throw new AlreadyFarmInvitedException(inviteeEmail);
    }

    // 토큰 발행
    InvitationTokenService.InvitationToken token = invitationTokenService.generate();

    // 메일 발송
  }
}
