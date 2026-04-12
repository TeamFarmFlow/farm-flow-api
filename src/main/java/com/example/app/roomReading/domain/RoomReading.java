package com.example.app.roomReading.domain;

import com.example.app.cultivationCycle.domain.CultivationCycle;
import com.example.app.room.domain.Room;
import com.example.app.shared.entity.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.util.Objects;
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
      @Index(name = "idx_room_reading_room_created_at", columnList = "room_id,created_at"),
      @Index(
          name = "idx_room_reading_cycle_created_at",
          columnList = "cultivation_cycle_id,created_at")
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

  @Column(name = "temperature", nullable = false, precision = 5, scale = 2)
  private BigDecimal temperature;

  @Column(name = "humidity", nullable = false, precision = 5, scale = 2)
  private BigDecimal humidity;

  @Column(name = "co2", nullable = false, precision = 8, scale = 2)
  private BigDecimal co2;

  @Column(name = "memo", length = 500)
  private String memo;

  public static RoomReading create(
      Room room,
      CultivationCycle cultivationCycle,
      BigDecimal temperature,
      BigDecimal humidity,
      BigDecimal co2,
      String memo) {
    validateCultivationCycleRoom(room, cultivationCycle);

    return RoomReading.builder()
        .room(room)
        .cultivationCycle(cultivationCycle)
        .temperature(temperature)
        .humidity(humidity)
        .co2(co2)
        .memo(normalizeMemo(memo))
        .build();
  }

  public void update(
      CultivationCycle cultivationCycle,
      BigDecimal temperature,
      BigDecimal humidity,
      BigDecimal co2,
      String memo) {
    validateCultivationCycleRoom(this.room, cultivationCycle);

    this.cultivationCycle = cultivationCycle;
    this.temperature = temperature;
    this.humidity = humidity;
    this.co2 = co2;
    this.memo = normalizeMemo(memo);
  }

  private static void validateCultivationCycleRoom(Room room, CultivationCycle cultivationCycle) {
    if (cultivationCycle == null) {
      return;
    }

    Long roomId = room.getId();
    Long cultivationCycleRoomId = cultivationCycle.getRoom().getId();
    if (roomId != null && cultivationCycleRoomId != null) {
      if (!Objects.equals(roomId, cultivationCycleRoomId)) {
        throw new IllegalArgumentException("Cultivation cycle does not belong to the room.");
      }
      return;
    }

    if (cultivationCycle.getRoom() != room) {
      throw new IllegalArgumentException("Cultivation cycle does not belong to the room.");
    }
  }

  private static String normalizeMemo(String memo) {
    if (memo == null) {
      return null;
    }

    String trimmedMemo = memo.trim();
    return trimmedMemo.isEmpty() ? null : trimmedMemo;
  }
}
