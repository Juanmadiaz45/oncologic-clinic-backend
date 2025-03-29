package com.oncologic.clinic.repository.user;

import com.oncologic.clinic.entity.user.Role;
import com.oncologic.clinic.entity.user.RolePermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RolePermissionRepository extends JpaRepository<RolePermission, RolePermission.RolePermissionId> {
    List<RolePermission> findByRole(Role role);

    void deleteByRole(Role existingRole);

    long countByRole(Role role);

    boolean existsByPermissionId(Long permissionId);
}
