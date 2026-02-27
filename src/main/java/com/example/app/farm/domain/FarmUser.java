package com.example.app.farm.domain;

import com.example.app.core.entity.BaseTimeEntity;
import com.example.app.user.domain.User;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "farm_users")
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

  public FarmUser(Farm farm, User user) {
    this.id = new FarmUserId(farm.getId(), user.getId());
    this.farm = farm;
    this.user = user;
  }
}
