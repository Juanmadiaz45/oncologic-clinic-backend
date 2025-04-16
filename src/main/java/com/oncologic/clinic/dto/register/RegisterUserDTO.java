package com.oncologic.clinic.dto.register;

import lombok.Data;

import java.util.Set;

@Data
public class RegisterUserDTO {
    private String username;
    private String password;
    private Set<Long> roleIds;
}
