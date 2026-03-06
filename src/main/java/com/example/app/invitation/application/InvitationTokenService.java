package com.example.app.invitation.application;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;
import org.springframework.stereotype.Component;

@Component
public class InvitationTokenService {
  private final SecureRandom random = new SecureRandom();

  public InvitationToken generate() {
    byte[] bytes = new byte[32];
    random.nextBytes(bytes);

    String raw = Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    String hash = sha256(raw);
    return new InvitationToken(raw, hash);
  }

  public String hash(String rawToken) {
    return sha256(rawToken);
  }

  private String sha256(String raw) {
    try {
      MessageDigest digest = MessageDigest.getInstance("SHA-256");
      byte[] hashed = digest.digest(raw.getBytes(StandardCharsets.UTF_8));
      return Base64.getUrlEncoder().withoutPadding().encodeToString(hashed);
    } catch (Exception e) {
      throw new IllegalStateException("Token hashing failed", e);
    }
  }

  public record InvitationToken(String rawToken, String tokenHash) {}
}
