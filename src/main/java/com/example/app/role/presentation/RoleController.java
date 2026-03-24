package com.example.app.role.presentation;

import com.example.app.core.jwt.CustomUserDetails;
import com.example.app.role.application.RoleService;
import com.example.app.role.presentation.request.RoleRegisterRequest;
import com.example.app.role.presentation.request.RoleUpdateRequest;
import com.example.app.role.presentation.response.RoleRegisterResponse;
import com.example.app.role.presentation.response.RoleUpdateResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@Tag(name = "역할 관리")
@RestController
@RequiredArgsConstructor
@RequestMapping("/farms/{farmId}/role")
public class RoleController {

  private final RoleService roleService;

  @Operation(summary = "역할 등록")
  @PostMapping("register")
  public ResponseEntity<RoleRegisterResponse> register(
      @PathVariable Long farmId,
      @Valid @RequestBody RoleRegisterRequest request,
      Authentication authentication) {
    Long userId = ((CustomUserDetails) authentication.getPrincipal()).getId();
    return ResponseEntity.ok(roleService.register(request.toCommand(), farmId, userId));
  }

  // 역할 조회

  @Operation(summary = "역할 수정")
  @PutMapping("{id}")
  public ResponseEntity<RoleUpdateResponse> update(
      @PathVariable Long id,
      @PathVariable Long farmId,
      Authentication authentication,
      @Valid @RequestBody RoleUpdateRequest request) {
    Long userId = ((CustomUserDetails) authentication.getPrincipal()).getId();
    return ResponseEntity.ok(roleService.update(id, request.toCommand(), farmId, userId));
  }

  @Operation(summary = "역할 삭제")
  @DeleteMapping("{id}")
  public ResponseEntity<Void> delete(
      @PathVariable Long id, @PathVariable Long farmId, Authentication authentication) {
    Long userId = ((CustomUserDetails) authentication.getPrincipal()).getId();
    roleService.delete(id, farmId, userId);
    return ResponseEntity.noContent().build();
  }
}
