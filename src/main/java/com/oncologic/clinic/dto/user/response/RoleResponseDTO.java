package com.oncologic.clinic.dto.user.response;

import lombok.Data;
import java.util.Set;

@Data
public class RoleResponseDTO {
    private Long id;
    private String name;
    private Set<PermissionResponseDTO> permissions;
}
