package com.example.app.farm.presentation;

import com.example.app.shared.dto.response.PageResponse;
import com.example.app.core.jwt.CustomUserDetails;
import com.example.app.farm.application.FarmService;
import com.example.app.farm.presentation.dto.request.FarmRegisterRequest;
import com.example.app.farm.presentation.dto.request.FarmUpdateRequest;
import com.example.app.farm.presentation.dto.response.FarmDetailResponse;
import com.example.app.farm.presentation.dto.response.FarmListResponse;
import com.example.app.farm.presentation.dto.response.FarmRegisterResponse;
import com.example.app.farm.presentation.dto.response.FarmUpdateResponse;
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
  @PostMapping("register")
  public ResponseEntity<FarmRegisterResponse> register(
      @Valid @RequestBody FarmRegisterRequest request, Authentication authentication) {
    Long userId = ((CustomUserDetails) authentication.getPrincipal()).getId();
    return ResponseEntity.ok(farmService.register(request.toCommand(), userId));
  }

  @Operation(summary = "농장 목록 조회")
  @GetMapping()
  public ResponseEntity<PageResponse<FarmListResponse>> getFarms(
      Authentication authentication,
      @ParameterObject
          @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC)
          Pageable pageable) {
    Long userId = ((CustomUserDetails) authentication.getPrincipal()).getId();
    Page<FarmListResponse> page = farmService.getFarms(userId, pageable);
    return ResponseEntity.ok(PageResponse.from(page));
  }

  @Operation(summary = "농장 정보 조회")
  @GetMapping("{id}")
  public ResponseEntity<FarmDetailResponse> getFarm(
      @PathVariable Long id, Authentication authentication) {
    Long userId = ((CustomUserDetails) authentication.getPrincipal()).getId();
    var result = farmService.getFarm(id, userId);
    return ResponseEntity.ok(result);
  }

  @Operation(summary = "농장 수정")
  @PutMapping("{id}")
  public ResponseEntity<FarmUpdateResponse> updateFarm(
      @Valid @RequestBody FarmUpdateRequest request,
      @PathVariable Long id,
      Authentication authentication) {
    Long userId = ((CustomUserDetails) authentication.getPrincipal()).getId();
    return ResponseEntity.ok(farmService.update(request.toCommand(), id, userId));
  }

  @Operation(summary = "농장 삭제")
  @DeleteMapping("{id}")
  public ResponseEntity<Void> deleteFarm(@PathVariable Long id, Authentication authentication) {
    Long userId = ((CustomUserDetails) authentication.getPrincipal()).getId();
    farmService.deleteFarm(id, userId);
    return ResponseEntity.noContent().build();
  }
}
