package com.oncologic.clinic.service.user.impl;

import com.oncologic.clinic.entity.user.Permission;
import com.oncologic.clinic.entity.user.Role;
import com.oncologic.clinic.entity.user.RolePermission;
import com.oncologic.clinic.repository.user.PermissionRepository;
import com.oncologic.clinic.repository.user.RolePermissionRepository;
import com.oncologic.clinic.repository.user.RoleRepository;
import com.oncologic.clinic.repository.user.UserRoleRepository;
import com.oncologic.clinic.service.user.RoleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final RolePermissionRepository rolePermissionRepository;
    private final UserRoleRepository userRoleRepository;

    public RoleServiceImpl(RoleRepository roleRepository, PermissionRepository permissionRepository, RolePermissionRepository rolePermissionRepository, UserRoleRepository userRoleRepository) {
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
        this.rolePermissionRepository = rolePermissionRepository;
        this.userRoleRepository = userRoleRepository;
    }

    @Override
    public Role createRole(Role role, Set<Long> permissionIds) {
        if (permissionIds == null || permissionIds.isEmpty()) {
            throw new IllegalArgumentException("Un rol debe tener al menos un permiso");
        }

        Set<Permission> permissions = new HashSet<>(permissionRepository.findAllById(permissionIds));
        if (permissions.isEmpty()) {
            throw new IllegalArgumentException("Los permisos proporcionados no existen");
        }

        Role savedRole = roleRepository.save(role);

        for (Permission permission : permissions) {
            RolePermission rolePermission = new RolePermission();
            rolePermission.setId(new RolePermission.RolePermissionId(savedRole.getId(), permission.getId()));
            rolePermission.setRole(savedRole);
            rolePermission.setPermission(permission);
            rolePermissionRepository.save(rolePermission);
        }

        return savedRole;
    }

    @Override
    public Role updateRole(Role role, Set<Long> permissionIds) {
        if (permissionIds == null || permissionIds.isEmpty()) {
            throw new IllegalArgumentException("Un rol debe tener al menos un permiso");
        }

        Role existingRole = roleRepository.findById(role.getId()).orElseThrow(() -> new RuntimeException("Rol no encontrado"));

        existingRole.setName(role.getName());

        Set<Permission> permissions = new HashSet<>(permissionRepository.findAllById(permissionIds));
        if (permissions.isEmpty()) {
            throw new IllegalArgumentException("Los permisos proporcionados no existen");
        }

        rolePermissionRepository.deleteByRole(existingRole);

        for (Permission permission : permissions) {
            RolePermission rolePermission = new RolePermission();
            rolePermission.setId(new RolePermission.RolePermissionId(existingRole.getId(), permission.getId()));
            rolePermission.setRole(existingRole);
            rolePermission.setPermission(permission);
            rolePermissionRepository.save(rolePermission);
        }

        return roleRepository.save(existingRole);
    }

    @Override
    public void deleteRole(Long id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Rol no encontrado"));

        // Verifica si hay usuarios asignados a través de UserRole
        boolean hasUsers = userRoleRepository.existsByRoleId(id);

        if (hasUsers) {
            throw new RuntimeException("No se puede eliminar el rol porque tiene usuarios asignados");
        }

        // Elimina primero las relaciones con permisos
        rolePermissionRepository.deleteByRole(role);

        // Finalmente elimina el rol
        roleRepository.delete(role);
    }

    @Override
    public Role getRoleById(Long id) {
        return roleRepository.findById(id).orElseThrow(() -> new RuntimeException("Rol no encontrado"));
    }

    @Override
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    @Override
    public Role addPermissionsToRole(Long roleId, Set<Long> permissionIds) {
        Role role = roleRepository.findById(roleId).orElseThrow(() -> new RuntimeException("Rol no encontrado"));

        Set<Permission> permissions = new HashSet<>(permissionRepository.findAllById(permissionIds));

        for (Permission permission : permissions) {
            RolePermission rolePermission = new RolePermission();
            rolePermission.setId(new RolePermission.RolePermissionId(role.getId(), permission.getId()));
            rolePermission.setRole(role);
            rolePermission.setPermission(permission);
            rolePermissionRepository.save(rolePermission);
        }

        return roleRepository.save(role);
    }

    @Override
    public Role removePermissionsFromRole(Long roleId, Set<Long> permissionIds) {
        Role role = roleRepository.findById(roleId).orElseThrow(() -> new RuntimeException("Rol no encontrado"));

        // Verificar que todos los permisos existen para este rol
        for (Long permissionId : permissionIds) {
            if (!rolePermissionRepository.existsById(new RolePermission.RolePermissionId(roleId, permissionId))) {
                throw new RuntimeException("Uno o más permisos no están asociados al rol");
            }
        }

        long remainingPermissions = rolePermissionRepository.countByRole(role) - permissionIds.size();
        if (remainingPermissions <= 0) {
            throw new IllegalStateException("Un rol debe tener al menos un permiso");
        }

        for (Long permissionId : permissionIds) {
            rolePermissionRepository.deleteById(new RolePermission.RolePermissionId(roleId, permissionId));
        }

        return roleRepository.save(role);
    }

}
