package com.oncologic.clinic.service.user.impl;

import com.oncologic.clinic.dto.user.RoleDTO;
import com.oncologic.clinic.dto.user.response.PermissionResponseDTO;
import com.oncologic.clinic.dto.user.response.RoleResponseDTO;
import com.oncologic.clinic.entity.user.Permission;
import com.oncologic.clinic.entity.user.Role;
import com.oncologic.clinic.entity.user.RolePermission;
import com.oncologic.clinic.exception.runtime.user.PermissionNotFoundException;
import com.oncologic.clinic.exception.runtime.user.RoleNotFoundException;
import com.oncologic.clinic.mapper.user.PermissionMapper;
import com.oncologic.clinic.mapper.user.RoleMapper;
import com.oncologic.clinic.repository.user.PermissionRepository;
import com.oncologic.clinic.repository.user.RolePermissionRepository;
import com.oncologic.clinic.repository.user.RoleRepository;
import com.oncologic.clinic.repository.user.UserRoleRepository;
import com.oncologic.clinic.entity.user.RolePermission.RolePermissionId;
import com.oncologic.clinic.service.user.RoleService;
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
            throw new IllegalArgumentException("A role must have at least one permission");
        }

        Set<Permission> permissions = new HashSet<>(permissionRepository.findAllById(roleDTO.getPermissionIds()));
        if (permissions.isEmpty() || permissions.size() != roleDTO.getPermissionIds().size()) {
            throw new IllegalArgumentException("One or more provided permissions do not exist");
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
            throw new IllegalArgumentException("A role must have at least one permission");
        }

        Role existingRole = roleRepository.findById(id)
                .orElseThrow(() -> new RoleNotFoundException(id));

        roleMapper.updateRoleFromDto(roleDTO, existingRole);

        Set<Permission> permissions = new HashSet<>(permissionRepository.findAllById(roleDTO.getPermissionIds()));
        if (permissions.isEmpty() || permissions.size() != roleDTO.getPermissionIds().size()) {
            throw new IllegalArgumentException("One or more provided permissions do not exist");
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
                .orElseThrow(() -> new RoleNotFoundException(id));

        // Check if there are any assigned users
        boolean hasUsers = userRoleRepository.existsByRoleId(id);

        if (hasUsers) {
            throw new IllegalStateException("The role cannot be deleted because it has users assigned to it.");
        }

        // Removes relationships with permissions
        rolePermissionRepository.deleteByRole(role);

        // Delete the role
        roleRepository.delete(role);
    }

    @Override
    public RoleResponseDTO getRoleById(Long id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new RoleNotFoundException(id));

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
                .orElseThrow(() -> new RoleNotFoundException(roleId));

        if (permissionIds == null || permissionIds.isEmpty()) {
            throw new IllegalArgumentException("You must provide at least one permit");
        }

        Set<Permission> permissions = new HashSet<>(permissionRepository.findAllById(permissionIds));

        if (permissions.size() != permissionIds.size()) {
            throw new PermissionNotFoundException("One or more permissions do not exist");
        }

        addPermissionsToRole(role, permissions);
        role = roleRepository.save(role);

        return roleMapper.roleToRoleResponseDto(role);
    }

    @Override
    @Transactional
    public RoleResponseDTO removePermissionsFromRole(Long roleId, Set<Long> permissionIds) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new RoleNotFoundException(roleId));

        // Validate that not all permissions are removed
        long currentPermissionCount = role.getRolePermissions().size();
        if (currentPermissionCount - permissionIds.size() <= 0) {
            throw new IllegalStateException("A role must have at least one permission");
        }

        // Create a copy to avoid ConcurrentModificationException
        Set<RolePermission> permissionsToRemove = new HashSet<>();

        // Identify permissions to remove
        role.getRolePermissions().forEach(rolePermission -> {
            if (permissionIds.contains(rolePermission.getPermission().getId())) {
                permissionsToRemove.add(rolePermission);
            }
        });

        // Validate that all requested permissions exist in the role
        if (permissionsToRemove.size() != permissionIds.size()) {
            throw new PermissionNotFoundException("One or more permissions are not associated with the role");
        }

        // Remove collection permissions
        permissionsToRemove.forEach(rolePermission -> {
            role.getRolePermissions().remove(rolePermission);
            rolePermission.setRole(null); // Breaking a two-way relationship
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
                .orElseThrow(() -> new RoleNotFoundException(id));
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
