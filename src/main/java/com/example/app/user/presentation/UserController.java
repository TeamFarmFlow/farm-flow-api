package com.example.app.user.presentation;

import com.example.app.user.application.UserService;
import com.example.app.user.domain.User;
import com.example.app.user.presentation.dto.response.UserResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("users")
@RequiredArgsConstructor
@Tag(name = "사용자")
public class UserController {
  private final UserService userService;

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
