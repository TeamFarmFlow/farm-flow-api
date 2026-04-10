package com.example.app.roomReading.domain;

import com.example.app.cultivationCycle.domain.CultivationCycle;
import com.example.app.room.domain.Room;
import com.example.app.shared.entity.BaseTimeEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;
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
    name = "room_readings",
    indexes = {
      @Index(name = "idx_room_reading_room_recorded_at", columnList = "room_id,recorded_at"),
      @Index(name = "idx_room_reading_room_created_at", columnList = "room_id,created_at"),
      @Index(
          name = "idx_room_reading_cycle_recorded_at",
          columnList = "cultivation_cycle_id,recorded_at")
    })
@Builder
public class RoomReading extends BaseTimeEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "room_id", nullable = false)
  private Room room;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "cultivation_cycle_id")
  private CultivationCycle cultivationCycle;

  @Column(name = "recorded_at", nullable = false)
  private LocalDateTime recordedAt;

  @Column(name = "temperature", nullable = false, precision = 5, scale = 2)
  private BigDecimal temperature;

  @Column(name = "humidity", nullable = false, precision = 5, scale = 2)
  private BigDecimal humidity;

  @Column(name = "co2", nullable = false, precision = 8, scale = 2)
  private BigDecimal co2;

  @Column(name = "memo", length = 500)
  private String memo;

  @OneToMany(mappedBy = "roomReading", cascade = CascadeType.ALL, orphanRemoval = true)
  @Builder.Default
  private List<RoomReadingImage> images = new ArrayList<>();
}
