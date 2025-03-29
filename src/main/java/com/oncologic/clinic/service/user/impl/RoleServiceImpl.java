package com.oncologic.clinic.service.user.impl;

import com.oncologic.clinic.entity.user.Permission;
import com.oncologic.clinic.entity.user.Role;
import com.oncologic.clinic.entity.user.RolePermission;
import com.oncologic.clinic.repository.user.PermissionRepository;
import com.oncologic.clinic.repository.user.RolePermissionRepository;
import com.oncologic.clinic.repository.user.RoleRepository;
import com.oncologic.clinic.repository.user.UserRoleRepository;
import com.oncologic.clinic.entity.user.RolePermission.RolePermissionId;
import com.oncologic.clinic.service.user.RoleService;
import jakarta.persistence.EntityNotFoundException;
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

        addPermissionsToRole(savedRole, permissions);

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

        addPermissionsToRole(existingRole, permissions);

        return roleRepository.save(existingRole);
    }

    @Override
    public void deleteRole(Long id) {
        Role role = roleRepository.findById(id).orElseThrow(() -> new RuntimeException("Rol no encontrado"));

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

        addPermissionsToRole(role, permissions);

        return roleRepository.save(role);
    }

    @Override
    @Transactional
    public Role removePermissionsFromRole(Long roleId, Set<Long> permissionIds) {
        // Cargar el rol con sus permisos (usando fetch join para evitar N+1)
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new EntityNotFoundException("Rol no encontrado"));

        // Validar que no se eliminen todos los permisos
        long currentPermissionCount = role.getRolePermissions().size();
        if (currentPermissionCount - permissionIds.size() <= 0) {
            throw new IllegalStateException("Un rol debe tener al menos un permiso");
        }

        // Crear copia para evitar ConcurrentModificationException
        Set<RolePermission> permissionsToRemove = new HashSet<>();

        // Identificar permisos a eliminar
        role.getRolePermissions().forEach(rolePermission -> {
            if (permissionIds.contains(rolePermission.getPermission().getId())) {
                permissionsToRemove.add(rolePermission);
            }
        });

        // Validar que todos los permisos solicitados existen en el rol
        if (permissionsToRemove.size() != permissionIds.size()) {
            throw new EntityNotFoundException("Uno o más permisos no están asociados al rol");
        }

        // Eliminar los permisos de la colección (orphanRemoval se encargará de la eliminación física)
        permissionsToRemove.forEach(rolePermission -> {
            role.getRolePermissions().remove(rolePermission);
            rolePermission.setRole(null); // Romper relación bidireccional
        });

        return roleRepository.save(role);
    }

    private void addPermissionsToRole(Role savedRole, Set<Permission> permissions) {
        for (Permission permission : permissions) {
            RolePermissionId id = new RolePermissionId(savedRole.getId(), permission.getId());
            if (!rolePermissionRepository.existsById(id)) {
                RolePermission rolePermission = new RolePermission(id, savedRole, permission);
                savedRole.getRolePermissions().add(rolePermission);
            }
        }
    }

}
