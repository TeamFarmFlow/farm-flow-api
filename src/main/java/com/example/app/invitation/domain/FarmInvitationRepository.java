package com.example.app.invitation.domain;

import com.example.app.invitation.domain.enums.FarmInvitationStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FarmInvitationRepository extends JpaRepository<FarmInvitation, Long> {

  Boolean existsByFarmIdAndInviteeEmailAndStatus(
      Long farmId, String inviteeEmail, FarmInvitationStatus farmInvitationStatus);
}
