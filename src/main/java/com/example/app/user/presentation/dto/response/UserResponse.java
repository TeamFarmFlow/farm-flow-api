package com.example.app.user.presentation.dto.response;

import java.time.Instant;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserResponse {
  private Long id;
  private String name;
  private Instant createdAt;
  private Instant updatedAt;
}
