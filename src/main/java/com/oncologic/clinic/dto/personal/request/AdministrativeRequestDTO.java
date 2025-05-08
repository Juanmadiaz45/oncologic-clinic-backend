package com.oncologic.clinic.dto.personal.request;

import lombok.Data;

@Data
public class AdministrativeRequestDTO {
    private PersonalRequestDTO personalData;
    private String position;
    private String department;
}
