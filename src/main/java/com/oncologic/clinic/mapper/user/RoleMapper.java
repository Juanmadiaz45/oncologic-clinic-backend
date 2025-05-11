package com.oncologic.clinic.mapper.user;

import com.oncologic.clinic.dto.user.RoleDTO;
import com.oncologic.clinic.dto.user.response.PermissionResponseDTO;
import com.oncologic.clinic.dto.user.response.RoleResponseDTO;
import com.oncologic.clinic.entity.user.Role;
import com.oncologic.clinic.entity.user.RolePermission;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = {PermissionMapper.class})
public interface RoleMapper {
    RoleMapper INSTANCE = Mappers.getMapper(RoleMapper.class);

    @Mapping(target = "rolePermissions", ignore = true)
    @Mapping(target = "userRoles", ignore = true)
    Role roleDtoToRole(RoleDTO roleDTO);

    @Mapping(target = "permissions", source = "rolePermissions", qualifiedByName = "mapRolePermissionsToPermissionDTOs")
    RoleResponseDTO roleToRoleResponseDto(Role role);

    @Named("mapRolePermissionsToPermissionDTOs")
    default Set<PermissionResponseDTO> mapRolePermissionsToPermissionDTOs(Set<RolePermission> rolePermissions) {
        return rolePermissions.stream()
                .map(RolePermission::getPermission)
                .map(PermissionMapper.INSTANCE::permissionToPermissionResponseDto)
                .collect(Collectors.toSet());
    }

    @Mapping(target = "rolePermissions", ignore = true)
    @Mapping(target = "userRoles", ignore = true)
    void updateRoleFromDto(RoleDTO roleDTO, @org.mapstruct.MappingTarget Role role);
}
