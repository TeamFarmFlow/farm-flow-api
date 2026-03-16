package com.example.app.invitation.application;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;
import org.springframework.stereotype.Component;

@Component
public class InvitationCodeService {

  private static final String CHAR_POOL =
          "ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz23456789";
  private static final int CODE_LENGTH = 6;

  private final SecureRandom random = new SecureRandom();

  public InvitationCode generate() {
    String rawCode = generateRandomCode();
    String codeHash = sha256(rawCode);
    return new InvitationCode(rawCode, codeHash);
  }

  public String hash(String rawCode) {
    return sha256(normalize(rawCode));
  }

  private String generateRandomCode() {
    StringBuilder sb = new StringBuilder(CODE_LENGTH);
    for (int i = 0; i < CODE_LENGTH; i++) {
      int index = random.nextInt(CHAR_POOL.length());
      sb.append(CHAR_POOL.charAt(index));
    }
    return sb.toString();
  }

  private String normalize(String rawCode) {
    return rawCode == null ? null : rawCode.trim().toUpperCase();
  }

  private String sha256(String raw) {
    try {
      MessageDigest digest = MessageDigest.getInstance("SHA-256");
      byte[] hashed = digest.digest(raw.getBytes(StandardCharsets.UTF_8));
      return Base64.getUrlEncoder().withoutPadding().encodeToString(hashed);
    } catch (Exception e) {
      throw new IllegalStateException("Invitation code hashing failed", e);
    }
  }

  public record InvitationCode(String rawCode, String codeHash) {}
}
