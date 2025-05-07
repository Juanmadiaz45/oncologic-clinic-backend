package com.oncologic.clinic.mapper.user;

import com.oncologic.clinic.dto.user.*;
import com.oncologic.clinic.dto.user.request.PermissionRequestDTO;
import com.oncologic.clinic.dto.user.response.PermissionResponseDTO;
import com.oncologic.clinic.dto.user.update.PermissionUpdateDTO;
import com.oncologic.clinic.entity.user.Permission;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
    PermissionMapper INSTANCE = Mappers.getMapper(PermissionMapper.class);

    @Mapping(target = "rolePermissions", ignore = true)
    Permission permissionRequestDtoToPermission(PermissionRequestDTO permissionRequestDTO);

    PermissionResponseDTO permissionToPermissionResponseDto(Permission permission);

    @Mapping(target = "rolePermissions", ignore = true)
    void updatePermissionFromDto(PermissionUpdateDTO permissionUpdateDTO, @org.mapstruct.MappingTarget Permission permission);
}
