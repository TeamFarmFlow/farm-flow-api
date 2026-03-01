package com.example.app.farm.presentation;

import com.example.app.core.guard.UserTypeGuard;
import com.example.app.core.jwt.CustomUserDetails;
import com.example.app.farm.application.FarmService;
import com.example.app.farm.presentation.dto.request.FarmRegisterRequest;
import com.example.app.farm.presentation.dto.response.FarmRegisterResponse;
import com.example.app.user.domain.enums.UserType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

  // 농장 수정

  // 농장 삭제
}
