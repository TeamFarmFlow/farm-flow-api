package com.example.app.invitation.domain;

import com.example.app.invitation.domain.enums.FarmInvitationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface FarmInvitationRepository extends JpaRepository<FarmInvitation, Long> {

  Boolean existsByFarmIdAndInviteeEmailAndStatus(
      Long farmId, String inviteeEmail, FarmInvitationStatus farmInvitationStatus);

  @Query(
      """
select f from FarmInvitation f
where f.inviteeEmail = :email
and f.inviteCodeHash = :code
and f.status = :status
""")
  FarmInvitation findByEmailAndCode(String email, String code, FarmInvitationStatus status);
}
