package com.oncologic.clinic.mapper.user;

import com.oncologic.clinic.dto.user.UserDTO;
import com.oncologic.clinic.dto.user.response.RoleResponseDTO;
import com.oncologic.clinic.dto.user.response.UserResponseDTO;
import com.oncologic.clinic.entity.user.User;
import com.oncologic.clinic.entity.user.UserRole;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = {RoleMapper.class})
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(target = "userRoles", ignore = true)
    @Mapping(target = "patient", ignore = true)
    @Mapping(target = "personal", ignore = true)
    User userDtoToUser(UserDTO userDTO);

    @Mapping(target = "roles", source = "userRoles", qualifiedByName = "mapUserRolesToRoleDTOs")
    UserResponseDTO userToUserResponseDto(User user);

    @Named("mapUserRolesToRoleDTOs")
    default Set<RoleResponseDTO> mapUserRolesToRoleDTOs(Set<UserRole> userRoles) {
        return userRoles.stream()
                .map(UserRole::getRole)
                .map(RoleMapper.INSTANCE::roleToRoleResponseDto)
                .collect(Collectors.toSet());
    }

    @Mapping(target = "userRoles", ignore = true)
    @Mapping(target = "patient", ignore = true)
    @Mapping(target = "personal", ignore = true)
    void updateUserFromDto(UserDTO userDTO, @org.mapstruct.MappingTarget User user);
}
