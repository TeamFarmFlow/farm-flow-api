package com.example.app.role.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RoleRepository extends JpaRepository<Role, Long> {
    @Query(
            """
select r
from Role r
where r.farm.id = :farmId
and r.key = :roleKey

"""
    )
    Role findByFarmIdAndRoleKey(Long farmId, String roleKey);
}
