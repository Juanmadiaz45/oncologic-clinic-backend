package com.oncologic.clinic.dto.personal.response;

import lombok.Data;

@Data
public class AdministrativeResponseDTO extends PersonalResponseDTO {
    private String position;
    private String department;
}
