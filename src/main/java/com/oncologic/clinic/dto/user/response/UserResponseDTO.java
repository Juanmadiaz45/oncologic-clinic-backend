package com.oncologic.clinic.dto.user.response;

import lombok.Data;
import java.util.Set;

@Data
public class UserResponseDTO {
    private Long id;
    private String username;
    private Set<RoleResponseDTO> roles;
}