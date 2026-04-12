package com.example.app.roomReading.presentation;

import com.example.app.core.jwt.CustomUserDetails;
import com.example.app.roomReading.application.RoomReadingService;
import com.example.app.roomReading.presentation.dto.response.RoomReadingResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "환경 기록 관리")
@RestController
@RequiredArgsConstructor
@RequestMapping("{farmId}/room-readings")
public class RoomReadingQueryController {
  private final RoomReadingService roomReadingService;

  @Operation(summary = "환경 기록 목록 조회")
  @GetMapping
  public ResponseEntity<List<RoomReadingResponse>> getRoomReadings(
      @PathVariable("farmId") Long farmId,
      @RequestParam(value = "roomId", required = false) Long roomId,
      @RequestParam(value = "from", required = false) LocalDate from,
      @RequestParam(value = "to", required = false) LocalDate to,
      Authentication authentication) {
    Long userId = ((CustomUserDetails) authentication.getPrincipal()).getId();
    return ResponseEntity.ok(roomReadingService.getRoomReadings(farmId, userId, roomId, from, to));
  }
}
