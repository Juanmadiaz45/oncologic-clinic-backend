package com.oncologic.clinic.dto.info;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class UserListGroupedDTO {
    private List<UserInfoDTO> doctors;
    private List<UserInfoDTO> administratives;
    private List<UserInfoDTO> patients;
}
