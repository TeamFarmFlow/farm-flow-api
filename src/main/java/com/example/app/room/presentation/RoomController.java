package com.example.app.room.presentation;

import com.example.app.core.jwt.CustomUserDetails;
import com.example.app.room.application.RoomService;
import com.example.app.room.presentation.dto.request.RoomRegisterRequest;
import com.example.app.room.presentation.dto.request.RoomUpdateRequest;
import com.example.app.room.presentation.dto.response.RoomResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "생육동 관리")
@RestController
@RequiredArgsConstructor
@RequestMapping("{farmId}/rooms")
public class RoomController {
  private final RoomService roomService;

  @Operation(summary = "생육동 등록")
  @PostMapping
  public ResponseEntity<RoomResponse> register(
      @PathVariable("farmId") Long farmId,
      @Valid @RequestBody RoomRegisterRequest request,
      Authentication authentication) {
    Long userId = ((CustomUserDetails) authentication.getPrincipal()).getId();
    return ResponseEntity.ok(roomService.register(farmId, request.toCommand(), userId));
  }

  @Operation(summary = "생육동 목록 조회")
  @GetMapping
  public ResponseEntity<List<RoomResponse>> getAllRooms(@PathVariable("farmId") Long farmId,
                                                        Authentication authentication) {
    Long userId = ((CustomUserDetails) authentication.getPrincipal()).getId();
    return ResponseEntity.ok(roomService.getAllRooms(farmId, userId));
  }

  @Operation(summary = "생육동 단건 조회")
  @GetMapping("/{id}")
  public ResponseEntity<RoomResponse> getRoom(@PathVariable("farmId") Long farmId,
                                              @PathVariable("id") Long id,
                                              Authentication authentication) {
    Long userId = ((CustomUserDetails) authentication.getPrincipal()).getId();
    return ResponseEntity.ok(roomService.getRoom(farmId, userId, id));
  }

  @Operation(summary = "생육동 수정")
  @PatchMapping("/{id}")
  public ResponseEntity<RoomResponse> updateRoom(@PathVariable("farmId") Long farmId,
                                                 @PathVariable("id") Long id,
                                                 @Valid @RequestBody RoomUpdateRequest request,
                                                 Authentication authentication){

    Long userId = ((CustomUserDetails) authentication.getPrincipal()).getId();
    return ResponseEntity.ok(roomService.updateRoom(farmId, id, request.toCommand(), userId));
  }

  @Operation(summary = "생육동 삭제")
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteRoom(@PathVariable("farmId") Long farmId,
                                         @PathVariable("id") Long id,
                                         Authentication authentication) {
    Long userId = ((CustomUserDetails) authentication.getPrincipal()).getId();
    roomService.deleteRoom(id, farmId, userId);
    return ResponseEntity.noContent().build();
  }
}
