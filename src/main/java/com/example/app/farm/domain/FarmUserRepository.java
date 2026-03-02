package com.example.app.farm.domain;

import com.example.app.user.domain.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FarmUserRepository extends JpaRepository<FarmUser, FarmUserId> {
  @Query(
      """
              select u
              from FarmUser fu
              join fu.user u
              where fu.farm.id = :farmId
                and u.deletedAt is null
            """)
  List<User> findUsersByFarmId(@Param("farmId") Long farmId);
}
