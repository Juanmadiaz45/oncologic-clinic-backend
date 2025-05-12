package com.oncologic.clinic.mapper.user;

import com.oncologic.clinic.dto.user.PermissionDTO;
import com.oncologic.clinic.dto.user.response.PermissionResponseDTO;
import com.oncologic.clinic.entity.user.Permission;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface PermissionMapper {

    @Mapping(target = "rolePermissions", ignore = true)
    Permission permissionDtoToPermission(PermissionDTO permissionDTO);

    PermissionResponseDTO permissionToPermissionResponseDto(Permission permission);

    @Mapping(target = "rolePermissions", ignore = true)
    void updatePermissionFromDto(PermissionDTO permissionDTO, @MappingTarget Permission permission);
}

