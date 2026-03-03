package com.example.app.farm.domain;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FarmRepository extends JpaRepository<Farm, Long> {
  @Query(
      """
      select f
      from Farm f
      join f.farmUsers fu
      where fu.user.id = :userId
        and f.deletedAt is null
      """)
  Page<Farm> findAllByUserId(@Param("userId") Long userId, Pageable pageable);

  @Query(
      """
      select f
      from Farm f
      join f.farmUsers fu
      where fu.user.id = :userId
      and f.id = :id
      and f.deletedAt is null
      """)
  Optional<Farm> findByUserId(Long id, Long userId);
}
