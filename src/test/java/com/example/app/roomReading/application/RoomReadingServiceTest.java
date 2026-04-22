package com.example.app.roomReading.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import com.example.app.cultivationCycle.domain.CultivationCycle;
import com.example.app.cultivationCycle.domain.CultivationCycleRepository;
import com.example.app.farm.application.FarmAccessValidator;
import com.example.app.farm.domain.Farm;
import com.example.app.farm.domain.enums.FarmStatus;
import com.example.app.farmUser.domain.FarmUserRepository;
import com.example.app.farmUser.domain.enums.FarmUserStatus;
import com.example.app.role.domain.enums.PermissionKey;
import com.example.app.room.domain.Room;
import com.example.app.room.domain.RoomRepository;
import com.example.app.room.domain.enums.RoomStatus;
import com.example.app.room.domain.exception.InvalidRoomStatusException;
import com.example.app.roomReading.application.command.RoomReadingRegisterCommand;
import com.example.app.roomReading.application.command.RoomReadingUpdateCommand;
import com.example.app.roomReading.domain.RoomReading;
import com.example.app.roomReading.domain.RoomReadingRepository;
import com.example.app.roomReading.presentation.dto.response.RoomReadingResponse;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RoomReadingServiceTest {
  private static final Long FARM_ID = 1L;
  private static final Long ROOM_ID = 2L;
  private static final Long USER_ID = 3L;
  private static final Long ROOM_READING_ID = 4L;
  private static final Long CYCLE_ID = 5L;
  private static final ZoneId DEFAULT_ZONE_ID = ZoneId.of("Asia/Seoul");

  @Mock private RoomReadingRepository roomReadingRepository;
  @Mock private FarmAccessValidator farmAccessValidator;
  @Mock private RoomRepository roomRepository;
  @Mock private FarmUserRepository farmUserRepository;
  @Mock private CultivationCycleRepository cultivationCycleRepository;

  @InjectMocks private RoomReadingService roomReadingService;

  private Farm farm;
  private Room room;

  @BeforeEach
  void setUp() {
    farm = Farm.builder().name("FarmFlow").status(FarmStatus.ACTIVE).build();
    setField(farm, "id", FARM_ID);

    room = Room.create(farm, "1동", "재배동");
    setField(room, "id", ROOM_ID);
  }

  @Test
  void register_success() {
    RoomReadingRegisterCommand command =
        new RoomReadingRegisterCommand(
            decimal("21.50"), decimal("88.10"), decimal("1450.00"), "오전 셋팅");

    givenManagePermission(PermissionKey.ROOM_READING_CREATE);
    given(roomRepository.findByIdAndFarm_Id(ROOM_ID, FARM_ID)).willReturn(Optional.of(room));
    given(cultivationCycleRepository.findByRoom_IdAndRoom_Farm_IdAndStatusIn(
                eq(ROOM_ID), eq(FARM_ID), any()))
        .willReturn(Optional.empty());
    given(roomReadingRepository.save(any(RoomReading.class)))
        .willAnswer(
            invocation -> {
              RoomReading roomReading = invocation.getArgument(0);
              setField(roomReading, "id", ROOM_READING_ID);
              setBaseTimeField(roomReading, "createdAt", Instant.parse("2026-04-22T01:00:00Z"));
              return roomReading;
            });

    RoomReadingResponse response = roomReadingService.register(FARM_ID, ROOM_ID, USER_ID, command);

    assertThat(response.id()).isEqualTo(ROOM_READING_ID);
    assertThat(response.roomId()).isEqualTo(ROOM_ID);
    assertThat(response.temperature()).isEqualByComparingTo("21.50");
    assertThat(response.humidity()).isEqualByComparingTo("88.10");
    assertThat(response.co2()).isEqualByComparingTo("1450.00");
    assertThat(response.memo()).isEqualTo("오전 셋팅");
    assertThat(response.createdAt()).isEqualTo(Instant.parse("2026-04-22T01:00:00Z"));
  }

  @Test
  void register_autoLinksActiveCycle() {
    CultivationCycle cultivationCycle = CultivationCycle.create(room, LocalDate.of(2026, 4, 20));
    setField(cultivationCycle, "id", CYCLE_ID);

    givenManagePermission(PermissionKey.ROOM_READING_CREATE);
    given(roomRepository.findByIdAndFarm_Id(ROOM_ID, FARM_ID)).willReturn(Optional.of(room));
    given(
            cultivationCycleRepository.findByRoom_IdAndRoom_Farm_IdAndStatusIn(
                eq(ROOM_ID), eq(FARM_ID), any()))
        .willReturn(Optional.of(cultivationCycle));
    given(roomReadingRepository.save(any(RoomReading.class)))
        .willAnswer(
            invocation -> {
              RoomReading roomReading = invocation.getArgument(0);
              setField(roomReading, "id", ROOM_READING_ID);
              setBaseTimeField(roomReading, "createdAt", Instant.parse("2026-04-22T02:00:00Z"));
              return roomReading;
            });

    roomReadingService.register(
        FARM_ID,
        ROOM_ID,
        USER_ID,
        new RoomReadingRegisterCommand(
            decimal("22.10"), decimal("87.00"), decimal("1410.00"), "cycle link"));

    ArgumentCaptor<RoomReading> captor = ArgumentCaptor.forClass(RoomReading.class);
    verify(roomReadingRepository).save(captor.capture());
    assertThat(captor.getValue().getCultivationCycle()).isSameAs(cultivationCycle);
  }

  @Test
  void register_failsWhenRoomIsInactive() {
    Room inactiveRoom = Room.create(farm, "2동", "비활성");
    setField(inactiveRoom, "id", ROOM_ID);
    inactiveRoom.update("2동", "비활성", RoomStatus.INACTIVE);

    givenManagePermission(PermissionKey.ROOM_READING_CREATE);
    given(roomRepository.findByIdAndFarm_Id(ROOM_ID, FARM_ID)).willReturn(Optional.of(inactiveRoom));

    assertThatThrownBy(
            () ->
                roomReadingService.register(
                    FARM_ID,
                    ROOM_ID,
                    USER_ID,
                    new RoomReadingRegisterCommand(
                        decimal("20.00"), decimal("80.00"), decimal("1300.00"), null)))
        .isInstanceOf(InvalidRoomStatusException.class);

    verify(roomReadingRepository, never()).save(any(RoomReading.class));
  }

  @Test
  void getRoomReading_success() {
    RoomReading roomReading = createRoomReading(room, null, ROOM_READING_ID);
    setBaseTimeField(roomReading, "createdAt", Instant.parse("2026-04-22T03:00:00Z"));

    given(roomRepository.findByIdAndFarm_Id(ROOM_ID, FARM_ID)).willReturn(Optional.of(room));
    given(roomReadingRepository.findByIdAndRoom_Id(ROOM_READING_ID, ROOM_ID))
        .willReturn(Optional.of(roomReading));

    RoomReadingResponse response =
        roomReadingService.getRoomReading(ROOM_READING_ID, FARM_ID, ROOM_ID, USER_ID);

    assertThat(response.id()).isEqualTo(ROOM_READING_ID);
    assertThat(response.roomId()).isEqualTo(ROOM_ID);
    assertThat(response.createdAt()).isEqualTo(Instant.parse("2026-04-22T03:00:00Z"));
    assertThat(response.temperature()).isEqualByComparingTo("21.30");
    assertThat(response.memo()).isEqualTo("정상");

    verify(farmAccessValidator).validateActiveFarm(FARM_ID);
    verify(farmAccessValidator).validateActiveMember(FARM_ID, USER_ID);
  }

  @Test
  void getRoomReadings_filtersByRoomAndDateRange() {
    RoomReading first = createRoomReading(room, null, 11L);
    RoomReading second = createRoomReading(room, null, 12L);
    setBaseTimeField(first, "createdAt", Instant.parse("2026-04-21T00:00:00Z"));
    setBaseTimeField(second, "createdAt", Instant.parse("2026-04-20T00:00:00Z"));

    given(roomRepository.findByIdAndFarm_Id(ROOM_ID, FARM_ID)).willReturn(Optional.of(room));
    given(roomReadingRepository.findAllByFilter(any(), any(), any(), any()))
        .willReturn(List.of(first, second));

    LocalDate from = LocalDate.of(2026, 4, 20);
    LocalDate to = LocalDate.of(2026, 4, 22);

    List<RoomReadingResponse> responses =
        roomReadingService.getRoomReadings(FARM_ID, USER_ID, ROOM_ID, from, to);

    ArgumentCaptor<Instant> fromCaptor = ArgumentCaptor.forClass(Instant.class);
    ArgumentCaptor<Instant> toCaptor = ArgumentCaptor.forClass(Instant.class);
    verify(roomReadingRepository)
        .findAllByFilter(eq(FARM_ID), eq(ROOM_ID), fromCaptor.capture(), toCaptor.capture());

    assertThat(fromCaptor.getValue()).isEqualTo(from.atStartOfDay(DEFAULT_ZONE_ID).toInstant());
    assertThat(toCaptor.getValue())
        .isEqualTo(to.plusDays(1).atStartOfDay(DEFAULT_ZONE_ID).toInstant());
    assertThat(responses).hasSize(2);
    assertThat(responses).extracting(RoomReadingResponse::id).containsExactly(11L, 12L);
  }

  @Test
  void updateRoomReading_clearsCultivationCycleWhenCommandHasNull() {
    CultivationCycle cultivationCycle = CultivationCycle.create(room, LocalDate.of(2026, 4, 20));
    setField(cultivationCycle, "id", CYCLE_ID);

    RoomReading roomReading = createRoomReading(room, cultivationCycle, ROOM_READING_ID);
    setBaseTimeField(roomReading, "createdAt", Instant.parse("2026-04-22T04:00:00Z"));

    givenManagePermission(PermissionKey.ROOM_READING_UPDATE);
    given(roomRepository.findByIdAndFarm_Id(ROOM_ID, FARM_ID)).willReturn(Optional.of(room));
    given(roomReadingRepository.findByIdAndRoom_Id(ROOM_READING_ID, ROOM_ID))
        .willReturn(Optional.of(roomReading));

    RoomReadingResponse response =
        roomReadingService.updateRoomReading(
            ROOM_READING_ID,
            FARM_ID,
            ROOM_ID,
            USER_ID,
            new RoomReadingUpdateCommand(
                null, decimal("19.50"), decimal("75.00"), decimal("1200.00"), "  "));

    assertThat(roomReading.getCultivationCycle()).isNull();
    assertThat(response.temperature()).isEqualByComparingTo("19.50");
    assertThat(response.humidity()).isEqualByComparingTo("75.00");
    assertThat(response.co2()).isEqualByComparingTo("1200.00");
    assertThat(response.memo()).isNull();
    verify(cultivationCycleRepository, never())
        .findByIdAndRoom_IdAndRoom_Farm_Id(any(), any(), any());
  }

  @Test
  void deleteRoomReading_success() {
    RoomReading roomReading = createRoomReading(room, null, ROOM_READING_ID);

    givenManagePermission(PermissionKey.ROOM_READING_DELETE);
    given(roomRepository.findByIdAndFarm_Id(ROOM_ID, FARM_ID)).willReturn(Optional.of(room));
    given(roomReadingRepository.findByIdAndRoom_Id(ROOM_READING_ID, ROOM_ID))
        .willReturn(Optional.of(roomReading));

    roomReadingService.deleteRoomReading(ROOM_READING_ID, FARM_ID, ROOM_ID, USER_ID);

    verify(roomReadingRepository).delete(roomReading);
  }

  private void givenManagePermission(PermissionKey permissionKey) {
    given(farmUserRepository.existsByFarmIdAndUserIdAndStatusAndPermissionKey(
                FARM_ID, USER_ID, FarmUserStatus.ACTIVE, permissionKey))
        .willReturn(true);
  }

  private RoomReading createRoomReading(Room room, CultivationCycle cultivationCycle, Long id) {
    RoomReading roomReading =
        RoomReading.create(
            room,
            cultivationCycle,
            decimal("21.30"),
            decimal("84.20"),
            decimal("1380.00"),
            "정상");
    setField(roomReading, "id", id);
    return roomReading;
  }

  private BigDecimal decimal(String value) {
    return new BigDecimal(value);
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
