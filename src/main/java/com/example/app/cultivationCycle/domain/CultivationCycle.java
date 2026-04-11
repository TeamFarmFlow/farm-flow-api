package com.example.app.cultivationCycle.domain;

import com.example.app.cultivationCycle.domain.enums.CultivationCycleStatus;
import com.example.app.room.domain.Room;
import com.example.app.roomReading.domain.RoomReading;
import com.example.app.shared.entity.BaseTimeEntity;
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
import java.time.LocalDate;
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
    name = "cultivation_cycles",
    indexes = {
      @Index(name = "idx_cycle_room_status", columnList = "room_id,status"),
      @Index(name = "idx_cycle_room_in_date", columnList = "room_id,in_date")
    })
@Builder
public class CultivationCycle extends BaseTimeEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "room_id", nullable = false)
  private Room room;

  @Column(name = "in_date", nullable = false)
  private LocalDate inDate;

  @Column(name = "thinning_date")
  private LocalDate thinningDate;

  @Column(name = "harvest_start_date")
  private LocalDate harvestStartDate;

  @Column(name = "out_date")
  private LocalDate outDate;

  @Enumerated(EnumType.STRING)
  @Column(name = "status", nullable = false, length = 20)
  private CultivationCycleStatus status;

  @Column(name = "note", length = 255)
  private String note;

  @OneToMany(mappedBy = "cultivationCycle")
  @Builder.Default
  private List<RoomReading> roomReadings = new ArrayList<>();

  public static CultivationCycle create(Room room, LocalDate inDate) {
    return CultivationCycle.builder()
        .room(room)
        .inDate(inDate)
        .status(CultivationCycleStatus.IN_PROGRESS)
        .build();
  }

  public void setThinningDate(String note, LocalDate date) {
    this.status = CultivationCycleStatus.THINNED;
    this.note = note;
    this.thinningDate = date;
  }

  public void setHarvestStartDate(String note, LocalDate date) {
    this.status = CultivationCycleStatus.HARVESTING;
    this.note = note;
    this.harvestStartDate = date;
  }

  public void setOutDate(String note, LocalDate date) {
    this.status = CultivationCycleStatus.COMPLETED;
    this.note = note;
    this.outDate = date;
  }
}
