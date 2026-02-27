
package com.example.app.core.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;


@ConfigurationProperties(prefix = "jwt")
@Getter
@Setter
public class JwtProperties {
  private String secret;
  private Long expirationSeconds;
}