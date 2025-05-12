package com.oncologic.clinic.service.user;

import com.oncologic.clinic.dto.user.RoleDTO;
import com.oncologic.clinic.dto.user.response.PermissionResponseDTO;
import com.oncologic.clinic.dto.user.response.RoleResponseDTO;
import com.oncologic.clinic.entity.user.Role;

import java.util.List;
import java.util.Set;

public interface RoleService {
    RoleResponseDTO createRole(RoleDTO roleDTO);
    RoleResponseDTO updateRole(Long id, RoleDTO roleDTO);
    void deleteRole(Long id);
    RoleResponseDTO getRoleById(Long id);
    List<Role> getRoles();
    List<RoleResponseDTO> getAllRoles();

    RoleResponseDTO addPermissionsToRole(Long roleId, Set<Long> permissionIds);
    RoleResponseDTO removePermissionsFromRole(Long roleId, Set<Long> permissionIds);
    List<PermissionResponseDTO> getPermissionsByRoleId(Long roleId);

    Role getRoleEntityById(Long id);

    List<Role> getAllRoleEntities();
}
