package com.example.app.auth.domain;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, String> {
  @Query(
      """
          select r
          from RefreshToken r
          join fetch r.user
          where r.id = :id AND r.expiredAt > current_timestamp
      """)
  Optional<RefreshToken> findValidByIdWithUser(@Param("id") String id);
}
