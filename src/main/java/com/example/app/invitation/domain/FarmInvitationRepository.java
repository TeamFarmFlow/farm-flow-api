package com.example.app.invitation.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface FarmInvitationRepository extends JpaRepository<FarmInvitation, Long> {
  Boolean existsByEmail(String inviteeEmail);
}
