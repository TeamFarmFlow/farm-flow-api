package com.example.app.role.domain;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RoleRepository extends JpaRepository<Role, Long> {
  @Query("""
select r
from Role r
where r.farm.id = :farmId
and r.key = :roleKey

""")
  Role findByFarmIdAndRoleKey(Long farmId, String roleKey);

  boolean existsByFarm_IdAndKey(Long farmId, String s);

  Optional<Role> findByIdAndFarm_Id(Long id, Long farmId);

  List<Role> findByFarmId(Long farmId);
}
