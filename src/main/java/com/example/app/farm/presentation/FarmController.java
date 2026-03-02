package com.example.app.farm.presentation;

import com.example.app.core.guard.UserTypeGuard;
import com.example.app.core.jwt.CustomUserDetails;
import com.example.app.farm.application.FarmService;
import com.example.app.farm.presentation.dto.request.FarmRegisterRequest;
import com.example.app.farm.presentation.dto.request.FarmUpdateRequest;
import com.example.app.farm.presentation.dto.response.FarmDetailResponse;
import com.example.app.farm.presentation.dto.response.FarmListResponse;
import com.example.app.farm.presentation.dto.response.FarmRegisterResponse;
import com.example.app.farm.presentation.dto.response.FarmUpdateResponse;
import com.example.app.user.domain.enums.UserType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
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
    return ResponseEntity.ok(farmService.register(request.toCommand(), userId));
  }

  @Operation(summary = "농장 목록 조회")
  @UserTypeGuard(UserType.OWNER)
  @GetMapping()
  public ResponseEntity<Page<FarmListResponse>> getFarms(
      Authentication authentication,
      @ParameterObject
          @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC)
          Pageable pageable) {
    Long userId = ((CustomUserDetails) authentication.getPrincipal()).getId();
    return ResponseEntity.ok(farmService.getFarms(userId, pageable));
  }

  @Operation(summary = "농장 정보 조회")
  @UserTypeGuard(UserType.OWNER)
  @GetMapping("{id}")
  public ResponseEntity<FarmDetailResponse> getFarm(@PathVariable Long id) {
    var result = farmService.getFarm(id);
    return ResponseEntity.ok(result);
  }

  @Operation(summary = "농장 수정")
  @UserTypeGuard(UserType.OWNER)
  @PutMapping("{id}")
  public ResponseEntity<FarmUpdateResponse> updateFarm(
      @Valid @RequestBody FarmUpdateRequest request, @PathVariable Long id) {
    return ResponseEntity.ok(farmService.update(request.toCommand(), id));
  }

  @Operation(summary = "농장 삭제")
  @UserTypeGuard(UserType.OWNER)
  @DeleteMapping("{id}")
  public ResponseEntity<Void> deleteFarm(@PathVariable Long id) {
    farmService.deleteFarm(id);
    return ResponseEntity.noContent().build();
  }
}
