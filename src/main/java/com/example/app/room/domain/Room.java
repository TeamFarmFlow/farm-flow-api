package com.example.app.room.domain;

import com.example.app.cultivationCycle.domain.CultivationCycle;
import com.example.app.farm.domain.Farm;
import com.example.app.room.domain.enums.RoomStatus;
import com.example.app.roomReading.domain.RoomReading;
import com.example.app.shared.entity.BaseTimeEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(
    name = "rooms",
    indexes = {
      @Index(name = "idx_room_farm_status", columnList = "farm_id,status"),
      @Index(name = "idx_room_farm_name", columnList = "farm_id,name")
    },
    uniqueConstraints = {
      @UniqueConstraint(
          name = "uk_room_farm_name",
          columnNames = {"farm_id", "name"})
    })
@Builder
public class Room extends BaseTimeEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "farm_id", nullable = false)
  private Farm farm;

  @Column(name = "name", nullable = false, length = 100)
  private String name;

  @Column(name = "description", length = 255)
  private String description;

  @Enumerated(EnumType.STRING)
  @Column(name = "status", nullable = false, length = 20)
  private RoomStatus status;

  @Column(name = "deleted_at")
  private Instant deletedAt;

  @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, orphanRemoval = true)
  @Builder.Default
  private List<CultivationCycle> cultivationCycles = new ArrayList<>();

  @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, orphanRemoval = true)
  @Builder.Default
  private List<RoomReading> roomReadings = new ArrayList<>();

  public static Room create(Farm farm, String name, String description) {
    return Room.builder()
        .farm(farm)
        .name(normalizeName(name))
        .description(normalizeDescription(description))
        .status(RoomStatus.ACTIVE)
        .build();
  }

  public void update(String name, String description, RoomStatus status) {
    this.name = normalizeName(name);
    this.description = normalizeDescription(description);
    this.status = status;
    if (status != RoomStatus.DELETED) {
      this.deletedAt = null;
    }
  }

  public void delete() {
    this.status = RoomStatus.DELETED;
    this.deletedAt = Instant.now();
    this.name = this.id + "_DELETED_" + this.deletedAt.toEpochMilli();
  }

  private static String normalizeName(String name) {
    return name.trim();
  }

  private static String normalizeDescription(String description) {
    if (description == null) {
      return null;
    }

    String trimmedDescription = description.trim();
    return trimmedDescription.isEmpty() ? null : trimmedDescription;
  }
}
