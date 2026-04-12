package com.example.app.roomReading.presentation;

import com.example.app.core.jwt.CustomUserDetails;
import com.example.app.roomReading.application.RoomReadingService;
import com.example.app.roomReading.presentation.dto.request.RoomReadingRegisterRequest;
import com.example.app.roomReading.presentation.dto.request.RoomReadingUpdateRequest;
import com.example.app.roomReading.presentation.dto.response.RoomReadingResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@Tag(name = "환경 기록 관리")
@RestController
@RequiredArgsConstructor
@RequestMapping("{farmId}/rooms/{roomId}/room-reading")
public class RoomReadingController {
  private final RoomReadingService roomReadingService;

  @Operation(summary = "환경 기록 등록")
  @PostMapping()
  public ResponseEntity<RoomReadingResponse> register(
      @PathVariable("farmId") Long farmId,
      @PathVariable("roomId") Long roomId,
      @Valid @RequestBody RoomReadingRegisterRequest request,
      Authentication authentication) {
    Long userId = ((CustomUserDetails) authentication.getPrincipal()).getId();
    return ResponseEntity.ok(
        roomReadingService.register(farmId, roomId, userId, request.toCommand()));
  }

  @Operation(summary = "환경 기록 단건 조회")
  @GetMapping("/{id}")
  public ResponseEntity<RoomReadingResponse> getRoomReading(
      @PathVariable("id") Long id,
      @PathVariable("farmId") Long farmId,
      @PathVariable("roomId") Long roomId,
      Authentication authentication) {

    Long userId = ((CustomUserDetails) authentication.getPrincipal()).getId();
    return ResponseEntity.ok(roomReadingService.getRoomReading(id, farmId, roomId, userId));
  }

  @Operation(summary = "환경 기록 삭제")
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteRoomReading(
      @PathVariable("id") Long id,
      @PathVariable("farmId") Long farmId,
      @PathVariable("roomId") Long roomId,
      Authentication authentication) {
    Long userId = ((CustomUserDetails) authentication.getPrincipal()).getId();
    roomReadingService.deleteRoomReading(id, farmId, roomId, userId);
    return ResponseEntity.noContent().build();
  }

  @Operation(summary = "환경 기록 수정")
  @PatchMapping("/{id}")
  public ResponseEntity<RoomReadingResponse> updateRoomReading(
      @PathVariable("id") Long id,
      @PathVariable("farmId") Long farmId,
      @PathVariable("roomId") Long roomId,
      @Valid @RequestBody RoomReadingUpdateRequest request,
      Authentication authentication) {
    Long userId = ((CustomUserDetails) authentication.getPrincipal()).getId();
    return ResponseEntity.ok(
        roomReadingService.updateRoomReading(id, farmId, roomId, userId, request.toCommand()));
  }
}
