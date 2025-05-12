package com.oncologic.clinic.mapper.user;

import com.oncologic.clinic.dto.user.RoleDTO;
import com.oncologic.clinic.dto.user.response.PermissionResponseDTO;
import com.oncologic.clinic.dto.user.response.RoleResponseDTO;
import com.oncologic.clinic.entity.user.Permission;
import com.oncologic.clinic.entity.user.Role;
import com.oncologic.clinic.entity.user.RolePermission;
import org.mapstruct.*;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = {PermissionMapper.class})
public interface RoleMapper {

    @Mapping(target = "rolePermissions", ignore = true)
    @Mapping(target = "userRoles", ignore = true)
    Role roleDtoToRole(RoleDTO roleDTO);

    @Mapping(target = "permissions", source = "rolePermissions", qualifiedByName = "rolePermissionsToPermissionResponseDTOs")
    RoleResponseDTO roleToRoleResponseDto(Role role);

    @Named("rolePermissionsToPermissionResponseDTOs")
    default Set<PermissionResponseDTO> mapRolePermissionsToPermissionResponseDTOs(Set<RolePermission> rolePermissions) {
        if (rolePermissions == null) {
            return null;
        }

        return rolePermissions.stream()
                .map(RolePermission::getPermission)
                .map(this::permissionToPermissionResponseDTO)
                .collect(Collectors.toSet());
    }

    PermissionResponseDTO permissionToPermissionResponseDTO(Permission permission);

    @Mapping(target = "rolePermissions", ignore = true)
    @Mapping(target = "userRoles", ignore = true)
    void updateRoleFromDto(RoleDTO roleDTO, @MappingTarget Role role);
}