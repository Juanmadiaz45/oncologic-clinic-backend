package com.oncologic.clinic.service.user;

import com.oncologic.clinic.dto.user.request.RoleRequestDTO;
import com.oncologic.clinic.dto.user.response.PermissionResponseDTO;
import com.oncologic.clinic.dto.user.response.RoleResponseDTO;
import com.oncologic.clinic.dto.user.update.RoleUpdateDTO;
import com.oncologic.clinic.entity.user.Permission;
import com.oncologic.clinic.entity.user.Role;

import java.util.List;
import java.util.Set;

public interface RoleService {
    RoleResponseDTO createRole(RoleRequestDTO roleDTO);
    RoleResponseDTO updateRole(Long id, RoleUpdateDTO roleDTO);
    void deleteRole(Long id);
    RoleResponseDTO getRoleById(Long id);
    List<RoleResponseDTO> getAllRoles();

    RoleResponseDTO addPermissionsToRole(Long roleId, Set<Long> permissionIds);
    RoleResponseDTO removePermissionsFromRole(Long roleId, Set<Long> permissionIds);
    List<PermissionResponseDTO> getPermissionsByRoleId(Long roleId);

    Role getRoleEntityById(Long id);

    List<Role> getAllRoleEntities();
}
