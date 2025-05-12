package com.oncologic.clinic.mapper.user;

import com.oncologic.clinic.dto.user.PermissionDTO;
import com.oncologic.clinic.dto.user.response.PermissionResponseDTO;
import com.oncologic.clinic.entity.user.Permission;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface PermissionMapper {

    @Mapping(target = "rolePermissions", ignore = true)
    Permission permissionDtoToPermission(PermissionDTO permissionDTO);

    PermissionResponseDTO permissionToPermissionResponseDto(Permission permission);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "rolePermissions", ignore = true)
    void updatePermissionFromDto(PermissionDTO permissionDTO, @MappingTarget Permission permission);
}

