package com.example.app.farm.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FarmRepository extends JpaRepository<Farm, Long> {
    @Query("""
      select f
      from Farm f
      join f.farmUsers fu
      where fu.user.id = :userId
        and f.deletedAt is null
      """)
    Page<Farm> findByUserId(@Param("userId") Long userId, Pageable pageable);
}