package com.oncologic.clinic.dto.user.update;

import lombok.Data;
import java.util.Set;

@Data
public class RoleUpdateDTO {
    private String name;
    private Set<Long> permissionIds;
}
