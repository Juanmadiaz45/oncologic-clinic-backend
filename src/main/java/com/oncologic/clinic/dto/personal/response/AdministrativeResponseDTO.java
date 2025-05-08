package com.oncologic.clinic.dto.personal.response;

import lombok.Data;

@Data
public class AdministrativeResponseDTO {
    private PersonalResponseDTO personalData;
    private String position;
    private String department;
}
