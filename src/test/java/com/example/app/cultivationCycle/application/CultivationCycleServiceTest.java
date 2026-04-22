package com.example.app.cultivationCycle.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import com.example.app.cultivationCycle.domain.CultivationCycle;
import com.example.app.cultivationCycle.domain.CultivationCycleRepository;
import com.example.app.cultivationCycle.presentation.dto.response.CultivationCycleResponse;
import com.example.app.farm.application.FarmAccessValidator;
import com.example.app.farm.domain.Farm;
import com.example.app.farm.domain.enums.FarmStatus;
import com.example.app.farmUser.domain.FarmUserRepository;
import com.example.app.room.domain.Room;
import com.example.app.room.domain.RoomRepository;
import com.example.app.roomReading.domain.RoomReading;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CultivationCycleServiceTest {
  private static final Long FARM_ID = 1L;
  private static final Long ROOM_ID = 2L;
  private static final Long USER_ID = 3L;
  private static final Long CYCLE_ID = 4L;

  @Mock private CultivationCycleRepository cultivationCycleRepository;
  @Mock private FarmAccessValidator farmAccessValidator;
  @Mock private RoomRepository roomRepository;
  @Mock private FarmUserRepository farmUserRepository;

  @InjectMocks private CultivationCycleService cultivationCycleService;

  private Room room;

  @BeforeEach
  void setUp() {
    Farm farm = Farm.builder().name("FarmFlow").status(FarmStatus.ACTIVE).build();
    setField(farm, "id", FARM_ID);

    room = Room.create(farm, "1동", "재배동");
    setField(room, "id", ROOM_ID);
  }

  @Test
  void getCycle_includesRoomReadings() {
    CultivationCycle cultivationCycle = CultivationCycle.create(room, LocalDate.of(2026, 4, 20));
    setField(cultivationCycle, "id", CYCLE_ID);

    RoomReading firstReading =
        RoomReading.create(
            room,
            cultivationCycle,
            new BigDecimal("17.00"),
            new BigDecimal("82.00"),
            new BigDecimal("1100.00"),
            "첫 기록");
    setField(firstReading, "id", 10L);
    setBaseTimeField(firstReading, "createdAt", Instant.parse("2026-04-22T00:00:00Z"));

    RoomReading secondReading =
        RoomReading.create(
            room,
            cultivationCycle,
            new BigDecimal("16.00"),
            new BigDecimal("80.00"),
            new BigDecimal("1150.00"),
            "둘째 기록");
    setField(secondReading, "id", 11L);
    setBaseTimeField(secondReading, "createdAt", Instant.parse("2026-04-22T01:00:00Z"));

    cultivationCycle.getRoomReadings().addAll(List.of(firstReading, secondReading));

    given(roomRepository.findByIdAndFarm_Id(ROOM_ID, FARM_ID)).willReturn(Optional.of(room));
    given(cultivationCycleRepository.findByIdAndRoom_IdAndRoom_Farm_Id(CYCLE_ID, ROOM_ID, FARM_ID))
        .willReturn(Optional.of(cultivationCycle));

    CultivationCycleResponse response =
        cultivationCycleService.getCycle(CYCLE_ID, FARM_ID, ROOM_ID, USER_ID);

    assertThat(response.id()).isEqualTo(CYCLE_ID);
    assertThat(response.roomReadings()).hasSize(2);
    assertThat(response.roomReadings().get(0).id()).isEqualTo(10L);
    assertThat(response.roomReadings().get(0).roomId()).isEqualTo(ROOM_ID);
    assertThat(response.roomReadings().get(0).createdAt())
        .isEqualTo(Instant.parse("2026-04-22T00:00:00Z"));
    assertThat(response.roomReadings().get(1).memo()).isEqualTo("둘째 기록");

    verify(farmAccessValidator).validateActiveFarm(FARM_ID);
    verify(farmAccessValidator).validateActiveMember(FARM_ID, USER_ID);
  }

  private void setBaseTimeField(Object target, String fieldName, Object value) {
    Class<?> type = target.getClass();
    while (type != null) {
      try {
        Field field = type.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
        return;
      } catch (NoSuchFieldException ignored) {
        type = type.getSuperclass();
      } catch (IllegalAccessException e) {
        throw new IllegalStateException(e);
      }
    }
    throw new IllegalArgumentException("Field not found: " + fieldName);
  }

  private void setField(Object target, String fieldName, Object value) {
    try {
      Field field = target.getClass().getDeclaredField(fieldName);
      field.setAccessible(true);
      field.set(target, value);
    } catch (NoSuchFieldException | IllegalAccessException e) {
      throw new IllegalStateException(e);
    }
  }
}
