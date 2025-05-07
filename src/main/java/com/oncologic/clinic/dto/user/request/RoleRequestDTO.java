package com.oncologic.clinic.dto.user.request;

import lombok.Data;
import java.util.Set;

@Data
public class RoleRequestDTO {
    private String name;
    private Set<Long> permissionIds;
}
