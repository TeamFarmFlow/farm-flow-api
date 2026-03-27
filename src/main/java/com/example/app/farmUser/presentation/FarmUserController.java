package com.example.app.farmUser.presentation;

import com.example.app.core.jwt.CustomUserDetails;
import com.example.app.farmUser.application.FarmUserService;
import com.example.app.farmUser.presentation.request.FarmUserRoleUpdateRequest;
import com.example.app.farmUser.presentation.request.FarmUserStatusUpdateRequest;
import com.example.app.farmUser.presentation.response.FarmUserResponse;
import com.example.app.farmUser.presentation.response.FarmUserRoleUpdateResponse;
import com.example.app.farmUser.presentation.response.FarmUserStatusUpdateResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@Tag(name = "농장 멤버 관리")
@RestController
@RequiredArgsConstructor
@RequestMapping("/farms/{farmId}/farmUser")
public class FarmUserController {
  private final FarmUserService farmUserService;

  @Operation(summary = "농장 멤버 조회")
  @GetMapping
  public ResponseEntity<List<FarmUserResponse>> getFarmUsers(
      @PathVariable("farmId") Long farmId, Authentication authentication) {
    Long userId = ((CustomUserDetails) authentication.getPrincipal()).getId();
    return ResponseEntity.ok(farmUserService.getFarmUsers(farmId, userId));
  }

  @Operation(summary = "농장 멤버 역할 변경")
  @PutMapping("/{targetUserId}/role")
  public ResponseEntity<FarmUserRoleUpdateResponse> updateFarmUserRole(
      @PathVariable Long farmId,
      @PathVariable Long targetUserId,
      @RequestBody FarmUserRoleUpdateRequest request,
      Authentication authentication) {
    Long userId = ((CustomUserDetails) authentication.getPrincipal()).getId();
    return ResponseEntity.ok(
        farmUserService.updateFarmUserRole(farmId, targetUserId, userId, request.tocommand()));
  }

  @Operation(summary = "농장 멤버 삭제")
  @PutMapping("{targetUserId}/removeMember")
  public ResponseEntity<FarmUserStatusUpdateResponse> removeFarmUser(
      @PathVariable Long farmId,
      @PathVariable Long targetUserId,
      @RequestBody FarmUserStatusUpdateRequest request,
      Authentication authentication) {
    Long userId = ((CustomUserDetails) authentication.getPrincipal()).getId();
    return ResponseEntity.ok(
        farmUserService.removeFarmUser(farmId, targetUserId, userId, request.toCommand()));
  }
}
