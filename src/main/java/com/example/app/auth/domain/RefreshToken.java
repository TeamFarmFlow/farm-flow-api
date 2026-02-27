package com.example.app.auth.domain;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.example.app.user.domain.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "refresh_tokens")
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
  private LocalDateTime createdAt;

  @Column(name = "expired_at", nullable = false, columnDefinition = "DATETIME NOT NULL DEFAULT (CURRENT_TIMESTAMP + INTERVAL 20 DAY)")
  private LocalDateTime expiredAt;

  public static RefreshToken issue(Long userId) {
    RefreshToken refreshToken = new RefreshToken();

    refreshToken.user = User.builder().id(userId).build();
    refreshToken.expiredAt = LocalDateTime.now().plusDays(20);

    return refreshToken;
  }
}
