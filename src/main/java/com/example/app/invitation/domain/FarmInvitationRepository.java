package com.example.app.invitation.domain;

import com.example.app.invitation.domain.enums.FarmInvitationStatus;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FarmInvitationRepository extends JpaRepository<FarmInvitation, Long> {

  Boolean existsByFarmIdAndInviteeEmailAndStatus(
      Long farmId, String inviteeEmail, FarmInvitationStatus farmInvitationStatus);

  Optional<FarmInvitation> findByInviteCodeHash(String codeHash);
}
