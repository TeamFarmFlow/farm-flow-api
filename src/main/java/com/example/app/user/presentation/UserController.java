package com.example.app.user.presentation;

import com.example.app.user.presentation.dto.request.UserSignUpRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.app.user.application.UserService;
import com.example.app.user.domain.User;
import com.example.app.user.presentation.dto.response.UserResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("users")
@RequiredArgsConstructor
@Tag(name = "사용자")
public class UserController {
  private final UserService userService;

  @Operation(summary = "회원 가입")
  @PostMapping("/signUp")
  public ResponseEntity<Long> signUp(@Valid @RequestBody UserSignUpRequest request) {
    Long userId = userService.signUp(request);
    return ResponseEntity.ok(userId);
  }

  @Operation(summary = "회원 조회")
  @GetMapping("{id}")
  public UserResponse getMethodName(@PathVariable Long id) {
    User user = userService.getUserById(id);

    return UserResponse.builder()
        .id(user.getId())
        .name(user.getName())
        .createdAt(user.getCreatedAt())
        .updatedAt(user.getUpdatedAt())
        .build();
  }
}
