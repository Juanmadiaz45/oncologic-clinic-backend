package com.oncologic.clinic.dto.personal.request;

import lombok.Data;

@Data
public class AdministrativeRequestDTO extends PersonalRequestDTO {
    private String position;
    private String department;
}
