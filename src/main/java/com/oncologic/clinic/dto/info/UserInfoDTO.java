package com.oncologic.clinic.dto.info;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class UserInfoDTO {
    private String username;
    private String fullName;
    private String phoneNumber;
    private String email;
    private List<String> roles;
}
