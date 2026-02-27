package com.example.app.auth.domain;

import com.example.app.user.domain.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.Instant;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(
    name = "refresh_tokens",
    indexes = {
      @Index(name = "IDX_REFRESH_TOKENS_USER_ID", columnList = "user_id"),
      @Index(name = "IDX_REFRESH_TOKENS_EXPIRED_AT", columnList = "expired_at"),
      @Index(name = "IDX_REFRESH_TOKENS_USER_ID_EXPIRED_AT", columnList = "user_id, expired_at")
    })
@Builder
@EntityListeners(AuditingEntityListener.class)
public class RefreshToken {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private String id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private User user;

  @CreatedDate
  @Column(name = "created_at", nullable = false, updatable = false)
  private Instant createdAt;

  @Column(
      name = "expired_at",
      nullable = false,
      columnDefinition = "DATETIME NOT NULL DEFAULT (CURRENT_TIMESTAMP + INTERVAL 20 DAY)")
  private Instant expiredAt;

  public static RefreshToken issue(Long userId) {
    RefreshToken refreshToken = new RefreshToken();

    refreshToken.user = User.builder().id(userId).build();
    refreshToken.expiredAt = Instant.now().plusSeconds(20 * 24 * 60 * 60);

    return refreshToken;
  }
}
