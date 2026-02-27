package com.example.app.health;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class HealthController {
  private final HealthCheckService healthCheckService;

  @GetMapping()
  public HealthResponse healthCheck() {
    return healthCheckService.healthCheck();
  }
}
