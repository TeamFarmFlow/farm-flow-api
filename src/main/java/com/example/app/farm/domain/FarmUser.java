package com.example.app.farm.domain;

import com.example.app.farm.domain.enums.FarmUserStatus;
import com.example.app.role.domain.Role;
import com.example.app.shared.entity.BaseTimeEntity;
import com.example.app.user.domain.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "farm_users")
@Builder
public class FarmUser extends BaseTimeEntity {
  @EmbeddedId private FarmUserId id;

  @ManyToOne(fetch = FetchType.LAZY)
  @MapsId("farmId")
  @JoinColumn(name = "farm_id")
  private Farm farm;

  @ManyToOne(fetch = FetchType.LAZY)
  @MapsId("userId")
  @JoinColumn(name = "user_id")
  private User user;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "role_id", nullable = false)
  private Role role;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, columnDefinition = "VARCHAR(20) NOT NULL")
  private FarmUserStatus status;

  public FarmUser(Farm farm, User user) {
    this.id = new FarmUserId(farm.getId(), user.getId());
    this.farm = farm;
    this.user = user;
    this.status = FarmUserStatus.ACTIVE;
  }
}
