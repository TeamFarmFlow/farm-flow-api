package com.example.app.farm.presentation;

import com.example.app.core.guard.UserTypeGuard;
import com.example.app.core.jwt.CustomUserDetails;
import com.example.app.farm.application.FarmService;
import com.example.app.farm.presentation.dto.request.FarmRegisterRequest;
import com.example.app.farm.presentation.dto.request.FarmUpdateRequest;
import com.example.app.farm.presentation.dto.response.FarmRegisterResponse;
import com.example.app.farm.presentation.dto.response.FarmUpdateResponse;
import com.example.app.user.domain.enums.UserType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@Tag(name = "농장")
@RestController
@RequiredArgsConstructor
@RequestMapping("farms")
public class FarmController {
  private final FarmService farmService;

  @Operation(summary = "농장 등록")
  @UserTypeGuard(UserType.OWNER)
  @PostMapping("register")
  public ResponseEntity<FarmRegisterResponse> register(
      @Valid @RequestBody FarmRegisterRequest request, Authentication authentication) {
    Long userId = ((CustomUserDetails) authentication.getPrincipal()).getId();
    var result = farmService.register(request.toCommand(), userId);
    return ResponseEntity.ok(new FarmRegisterResponse(result.getId(), userId, result.getName()));
  }

  // 농장 조회
  //  @Operation(summary = "농장 목록 조회")
  //  @UserTypeGuard(UserType.OWNER)
  //  @GetMapping()
  //  public ResponseEntity<List<Farm>> getFarmList(@Valid Authentication authentication)

  @Operation(summary = "농장 수정")
  @UserTypeGuard(UserType.OWNER)
  @PutMapping("{id}")
  public ResponseEntity<FarmUpdateResponse> updateFarm(
      @Valid @RequestBody FarmUpdateRequest request,
      @PathVariable Long id,
      Authentication authentication) {
    Long userId = ((CustomUserDetails) authentication.getPrincipal()).getId();
    var result = farmService.update(request.toCommand(), id);
    return ResponseEntity.ok(
        new FarmUpdateResponse(result.getId(), userId, result.getName(), result.getStatus()));
  }

  @Operation(summary = "농장 삭제")
  @UserTypeGuard(UserType.OWNER)
  @DeleteMapping("{id}")
  public ResponseEntity<Void> deleteFarm(@PathVariable Long id) {
    farmService.deleteFarm(id);
    return ResponseEntity.noContent().build();
  }
}
