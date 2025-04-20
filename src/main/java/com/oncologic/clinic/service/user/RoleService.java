package com.oncologic.clinic.service.user;

import com.oncologic.clinic.entity.user.Permission;
import com.oncologic.clinic.entity.user.Role;

import java.util.List;
import java.util.Set;

public interface RoleService {
    Role createRole(Role role, Set<Long> permissionIds);

    Role updateRole(Role role, Set<Long> permissionIds);

    void deleteRole(Long id);

    Role getRoleById(Long id);

    List<Role> getAllRoles();

    Role addPermissionsToRole(Long roleId, Set<Long> permissionIds);

    Role removePermissionsFromRole(Long roleId, Set<Long> permissionIds);

    List<Permission> getPermissionsByRoleId(Long roleId);
}
