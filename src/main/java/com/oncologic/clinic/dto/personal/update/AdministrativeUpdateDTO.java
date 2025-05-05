package com.oncologic.clinic.dto.personal.update;

import lombok.Data;

@Data
public class AdministrativeUpdateDTO extends PersonalUpdateDTO {
    private String position;
    private String department;
}
