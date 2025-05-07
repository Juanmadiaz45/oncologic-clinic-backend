package com.oncologic.clinic.dto.user.request;

import lombok.Data;
import java.util.Set;

@Data
public class UserRequestDTO {
    private String username;
    private String password;
    private Set<Long> roleIds;
}