package com.oncologic.clinic.dto.personal;

import lombok.Data;

@Data
public class AdministrativeDTO {
    private PersonalDTO personalData;
    private String position;
    private String department;
}
