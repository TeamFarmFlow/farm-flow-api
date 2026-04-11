package com.example.app.cultivationCycle.presentation;

import com.example.app.core.jwt.CustomUserDetails;
import com.example.app.cultivationCycle.application.CultivationCycleService;
import com.example.app.cultivationCycle.presentation.dto.request.CultivationCycleMarkThinningRequest;
import com.example.app.cultivationCycle.presentation.dto.response.CultivationCycleResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@Tag(name = "생육동 사이클 관리")
@RestController
@RequiredArgsConstructor
@RequestMapping("{farmId}/rooms/{roomId}/cultivation-cycles")
public class CultivationCycleController {
  private final CultivationCycleService cultivationCycleService;

  @Operation(summary = "생육동 사이클 등록")
  @PostMapping
  public ResponseEntity<CultivationCycleResponse> register(
      @PathVariable("farmId") Long farmId,
      @PathVariable("roomId") Long roomId,
      Authentication authentication) {
    Long userId = ((CustomUserDetails) authentication.getPrincipal()).getId();
    return ResponseEntity.ok(cultivationCycleService.register(farmId, roomId, userId));
  }

  @Operation(summary = "생육동 사이클 목록 조회")
  @GetMapping
  public ResponseEntity<List<CultivationCycleResponse>> getCultivationCycles(
      @PathVariable("farmId") Long farmId,
      @PathVariable("roomId") Long roomId,
      Authentication authentication) {
    Long userId = ((CustomUserDetails) authentication.getPrincipal()).getId();
    return ResponseEntity.ok(cultivationCycleService.getCultivationCycles(farmId, roomId, userId));
  }

  @Operation(summary = "생육동 사이클 단건 조회")
  @GetMapping("/{id}")
  public ResponseEntity<CultivationCycleResponse> getCycle(
      @PathVariable("farmId") Long farmId,
      @PathVariable("roomId") Long roomId,
      @PathVariable("id") Long id,
      Authentication authentication) {

    Long userId = ((CustomUserDetails) authentication.getPrincipal()).getId();
    return ResponseEntity.ok(cultivationCycleService.getCycle(id, farmId, roomId, userId));
  }

  @Operation(summary = "솎기 처리 ")
  @PatchMapping("/{id}/thinning")
  public ResponseEntity<CultivationCycleResponse> markThinning(@PathVariable("farmId") Long farmId,
                                                               @PathVariable("roomId") Long roomId,
                                                               @PathVariable("id") Long id,
                                                               @Valid @RequestBody CultivationCycleMarkThinningRequest request,
                                                               Authentication authentication){
    Long userId = ((CustomUserDetails) authentication.getPrincipal()).getId();
    return ResponseEntity.ok(cultivationCycleService.markThinning(request.toCommand(), farmId, roomId, id, userId));
  }
}
