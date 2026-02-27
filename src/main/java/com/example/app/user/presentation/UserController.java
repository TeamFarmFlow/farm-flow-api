package com.example.app.user.presentation;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.app.user.application.UserService;
import com.example.app.user.domain.User;
import com.example.app.user.presentation.dto.response.UserResponse;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("users")
@RequiredArgsConstructor
public class UserController {
  private final UserService userService;

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
