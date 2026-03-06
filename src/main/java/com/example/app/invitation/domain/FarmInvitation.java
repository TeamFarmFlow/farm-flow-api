package com.example.app.invitation.domain;

import com.example.app.farm.domain.Farm;
import com.example.app.invitation.domain.enums.FarmInvitationStatus;
import com.example.app.role.domain.Role;
import com.example.app.shared.entity.BaseTimeEntity;
import com.example.app.user.domain.User;
import jakarta.persistence.*;
import java.time.Instant;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(
    name = "farm_invitations",
    indexes = {
      @Index(name = "idx_invite_farm_status", columnList = "farm_id,status"),
      @Index(name = "idx_invite_invitee_email", columnList = "invitee_email"),
      @Index(name = "idx_invite_token_hash", columnList = "token_hash")
    },
    uniqueConstraints = {
      @UniqueConstraint(
          name = "uk_invite_token_hash",
          columnNames = {"token_hash"})
    })
@Builder
public class FarmInvitation extends BaseTimeEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "farm_id", nullable = false)
  private Farm farm;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "inviter_user_id", nullable = false)
  private User inviter;

  @Column(name = "invitee_email", nullable = false, length = 120)
  private String inviteeEmail;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "invitee_user_id", nullable = true)
  private User inviteeUser;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "assinged_role_id", nullable = false)
  private Role assignedRole;

  @Enumerated(EnumType.STRING)
  @Column(name = "status", nullable = false, length = 20)
  private FarmInvitationStatus status;

  @Column(name = "token_hash", nullable = false, length = 128)
  private String tokenHash;

  @Column(name = "expires_at", nullable = false)
  private Instant expiresAt;

  @Column(name = "responded_at")
  private Instant respondedAt;
}
