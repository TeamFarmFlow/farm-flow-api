package com.example.app.farm.domain;

import com.example.app.core.entity.BaseTimeEntity;
import com.example.app.farm.domain.enums.FarmStatus;
import jakarta.persistence.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import lombok.*;

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

  public void update(String name, FarmStatus status) {
    this.name = name;
    this.status = status;
  }

  public void delete() {
    this.deletedAt = Instant.now();
    this.status = FarmStatus.DELETED;
  }
}
