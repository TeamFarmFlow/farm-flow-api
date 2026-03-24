package com.example.app.farm.domain;

import com.example.app.role.domain.enums.PermissionKey;
import com.example.app.user.domain.User;
import java.util.List;
import java.util.Optional;
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

  @Query(
      """
                        select u
                        from FarmUser fu
                        join fu.user u
                        join fu.role r
                        where fu.farm.id = :farmId
                          and u.id = :userId
                          and r.key = :roleKey
                    """)
  Optional<User> findOwnerUser(Long farmId, Long userId, String roleKey);

  boolean existsByFarm_IdAndUser_Email(Long farmId, String email);

  boolean existsByFarm_IdAndUser_Id(Long farmId, Long userId);

  @Query(
      """
    select case when count(fu) > 0 then true else false end
    from FarmUser fu
    join RolePermission rp on fu.role = rp.role
    where fu.farm.id = :farmId
                and fu.user.id = :userId
                and rp.id.permissionKey = :permissionKey
    """)
  boolean existsByUserIdAndPermissionKey(Long farmId, Long userId, PermissionKey permissionKey);

  boolean existsByRole_Id(Long id);
}
