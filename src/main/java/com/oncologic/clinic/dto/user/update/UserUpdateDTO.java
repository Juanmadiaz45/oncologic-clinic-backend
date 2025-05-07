package com.oncologic.clinic.dto.user.update;

import lombok.Data;
import java.util.Set;

@Data
public class UserUpdateDTO {
    private String username;
    private String password;
    private Set<Long> roleIds;
}