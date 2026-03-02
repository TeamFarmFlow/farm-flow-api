package com.example.app.farm.domain;

import com.example.app.core.entity.BaseTimeEntity;
import com.example.app.farm.domain.enums.FarmStatus;
import com.example.app.user.domain.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "farms")
@Builder
public class Farm extends BaseTimeEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(length = 120, nullable = false, unique = true)
  private String name;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, columnDefinition = "VARCHAR(20) NOT NULL")
  private FarmStatus status;

  @Column(nullable = true)
  private Instant deletedAt;

  @OneToMany(mappedBy = "farm", cascade = CascadeType.ALL, orphanRemoval = true)
  private final List<FarmUser> farmUsers = new ArrayList<>();

  public void addFarmUser(User user) {
    FarmUser farmUser = new FarmUser(this, user);
    this.farmUsers.add(farmUser);
  }

  public void addFarmUser(Long userId) {
    FarmUser farmUser = new FarmUser(this, User.farmOf(userId));
    this.farmUsers.add(farmUser);
  }

  public void update(String name, FarmStatus status) {
    this.name = name;
    this.status = status;
  }

  public void delete() {
    this.deletedAt = Instant.now();
    this.status = FarmStatus.DELETED;
  }
}
