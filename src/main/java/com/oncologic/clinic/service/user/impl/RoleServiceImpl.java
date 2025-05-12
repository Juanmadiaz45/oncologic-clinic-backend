package com.oncologic.clinic.service.user.impl;

import com.oncologic.clinic.dto.user.RoleDTO;
import com.oncologic.clinic.dto.user.response.PermissionResponseDTO;
import com.oncologic.clinic.dto.user.response.RoleResponseDTO;
import com.oncologic.clinic.entity.user.Permission;
import com.oncologic.clinic.entity.user.Role;
import com.oncologic.clinic.entity.user.RolePermission;
import com.oncologic.clinic.mapper.user.PermissionMapper;
import com.oncologic.clinic.mapper.user.RoleMapper;
import com.oncologic.clinic.repository.user.PermissionRepository;
import com.oncologic.clinic.repository.user.RolePermissionRepository;
import com.oncologic.clinic.repository.user.RoleRepository;
import com.oncologic.clinic.repository.user.UserRoleRepository;
import com.oncologic.clinic.entity.user.RolePermission.RolePermissionId;
import com.oncologic.clinic.service.user.RoleService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final RolePermissionRepository rolePermissionRepository;
    private final UserRoleRepository userRoleRepository;
    private final RoleMapper roleMapper;
    private final PermissionMapper permissionMapper;

    @Autowired
    public RoleServiceImpl(
            RoleRepository roleRepository,
            PermissionRepository permissionRepository,
            RolePermissionRepository rolePermissionRepository,
            UserRoleRepository userRoleRepository,
            RoleMapper roleMapper,
            PermissionMapper permissionMapper) {
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
        this.rolePermissionRepository = rolePermissionRepository;
        this.userRoleRepository = userRoleRepository;
        this.roleMapper = roleMapper;
        this.permissionMapper = permissionMapper;
    }

    @Override
    public RoleResponseDTO createRole(RoleDTO roleDTO) {
        if (roleDTO.getPermissionIds() == null || roleDTO.getPermissionIds().isEmpty()) {
            throw new IllegalArgumentException("Un rol debe tener al menos un permiso");
        }

        Set<Permission> permissions = new HashSet<>(permissionRepository.findAllById(roleDTO.getPermissionIds()));
        if (permissions.isEmpty() || permissions.size() != roleDTO.getPermissionIds().size()) {
            throw new IllegalArgumentException("Uno o más permisos proporcionados no existen");
        }

        Role role = roleMapper.roleDtoToRole(roleDTO);
        Role savedRole = roleRepository.save(role);

        addPermissionsToRole(savedRole, permissions);
        savedRole = roleRepository.save(savedRole);

        return roleMapper.roleToRoleResponseDto(savedRole);
    }

    @Override
    public RoleResponseDTO updateRole(Long id, RoleDTO roleDTO) {
        if (roleDTO.getPermissionIds() == null || roleDTO.getPermissionIds().isEmpty()) {
            throw new IllegalArgumentException("Un rol debe tener al menos un permiso");
        }

        Role existingRole = roleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Rol no encontrado con ID: " + id));

        roleMapper.updateRoleFromDto(roleDTO, existingRole);

        Set<Permission> permissions = new HashSet<>(permissionRepository.findAllById(roleDTO.getPermissionIds()));
        if (permissions.isEmpty() || permissions.size() != roleDTO.getPermissionIds().size()) {
            throw new IllegalArgumentException("Uno o más permisos proporcionados no existen");
        }

        rolePermissionRepository.deleteByRole(existingRole);
        existingRole.getRolePermissions().clear();

        addPermissionsToRole(existingRole, permissions);
        existingRole = roleRepository.save(existingRole);

        return roleMapper.roleToRoleResponseDto(existingRole);
    }

    @Override
    public void deleteRole(Long id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Rol no encontrado con ID: " + id));

        // Verifica si hay usuarios asignados
        boolean hasUsers = userRoleRepository.existsByRoleId(id);

        if (hasUsers) {
            throw new IllegalStateException("No se puede eliminar el rol porque tiene usuarios asignados");
        }

        // Elimina las relaciones con permisos
        rolePermissionRepository.deleteByRole(role);

        // Elimina el rol
        roleRepository.delete(role);
    }

    @Override
    public RoleResponseDTO getRoleById(Long id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Rol no encontrado con ID: " + id));

        return roleMapper.roleToRoleResponseDto(role);
    }

    @Override
    public List<RoleResponseDTO> getAllRoles() {
        return roleRepository.findAll().stream()
                .map(roleMapper::roleToRoleResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<Role> getRoles() {
        return roleRepository.findAll();
    }

    @Override
    public RoleResponseDTO addPermissionsToRole(Long roleId, Set<Long> permissionIds) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new EntityNotFoundException("Rol no encontrado con ID: " + roleId));

        if (permissionIds == null || permissionIds.isEmpty()) {
            throw new IllegalArgumentException("Debe proporcionar al menos un permiso");
        }

        Set<Permission> permissions = new HashSet<>(permissionRepository.findAllById(permissionIds));

        if (permissions.size() != permissionIds.size()) {
            throw new EntityNotFoundException("Uno o más permisos no existen");
        }

        addPermissionsToRole(role, permissions);
        role = roleRepository.save(role);

        return roleMapper.roleToRoleResponseDto(role);
    }

    @Override
    @Transactional
    public RoleResponseDTO removePermissionsFromRole(Long roleId, Set<Long> permissionIds) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new EntityNotFoundException("Rol no encontrado con ID: " + roleId));

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

        // Eliminar los permisos de la colección
        permissionsToRemove.forEach(rolePermission -> {
            role.getRolePermissions().remove(rolePermission);
            rolePermission.setRole(null); // Romper relación bidireccional
        });

        rolePermissionRepository.deleteAll(permissionsToRemove);
        Role updatedRole = roleRepository.save(role);

        return roleMapper.roleToRoleResponseDto(updatedRole);
    }

    @Override
    public List<PermissionResponseDTO> getPermissionsByRoleId(Long roleId) {
        return rolePermissionRepository.findPermissionsByRoleId(roleId).stream()
                .map(RolePermission::getPermission)
                .map(permissionMapper::permissionToPermissionResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public Role getRoleEntityById(Long id) {
        return roleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Rol no encontrado con ID: " + id));
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

    @Override
    public List<Role> getAllRoleEntities() {
        return roleRepository.findAll();
    }
}
