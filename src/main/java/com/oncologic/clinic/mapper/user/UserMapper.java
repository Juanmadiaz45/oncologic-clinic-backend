package com.oncologic.clinic.mapper.user;

import com.oncologic.clinic.dto.user.UserDTO;
import com.oncologic.clinic.dto.user.response.RoleResponseDTO;
import com.oncologic.clinic.dto.user.response.UserResponseDTO;
import com.oncologic.clinic.entity.user.Role;
import com.oncologic.clinic.entity.user.User;
import com.oncologic.clinic.entity.user.UserRole;
import org.mapstruct.*;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = {RoleMapper.class})
public interface UserMapper {

    @Mapping(target = "userRoles", ignore = true)
    @Mapping(target = "patient", ignore = true)
    @Mapping(target = "personal", ignore = true)
    User userDtoToUser(UserDTO userDTO);

    @Mapping(target = "roles", source = "userRoles", qualifiedByName = "userRolesToRoleResponseDTOs")
    UserResponseDTO userToUserResponseDto(User user);

    @Named("userRolesToRoleResponseDTOs")
    default Set<RoleResponseDTO> mapUserRolesToRoleResponseDTOs(Set<UserRole> userRoles) {
        if (userRoles == null) {
            return null;
        }

        return userRoles.stream()
                .map(UserRole::getRole)
                .map(this::roleToRoleResponseDTO)
                .collect(Collectors.toSet());
    }

    RoleResponseDTO roleToRoleResponseDTO(Role role);

    @Mapping(target = "userRoles", ignore = true)
    @Mapping(target = "patient", ignore = true)
    @Mapping(target = "personal", ignore = true)
    void updateUserFromDto(UserDTO userDTO, @MappingTarget User user);
}