package com.oncologic.clinic.dto.personal.update;

import com.oncologic.clinic.dto.personal.request.PersonalRequestDTO;
import lombok.Data;

@Data
public class AdministrativeUpdateDTO {
    private PersonalRequestDTO personalData;
    private String position;
    private String department;
}
