package com.oncologic.clinic.mapper.user;

import com.oncologic.clinic.dto.user.UserRoleDTO;
import com.oncologic.clinic.entity.user.UserRole;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserRoleMapper {
    UserRoleMapper INSTANCE = Mappers.getMapper(UserRoleMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "role", ignore = true)
    UserRole userRoleDtoToUserRole(UserRoleDTO userRoleDTO);

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "roleId", source = "role.id")
    UserRoleDTO userRoleToUserRoleDto(UserRole userRole);

    default UserRoleDTO toDto(Long userId, Long roleId) {
        UserRoleDTO dto = new UserRoleDTO();
        dto.setUserId(userId);
        dto.setRoleId(roleId);
        return dto;
    }
}
