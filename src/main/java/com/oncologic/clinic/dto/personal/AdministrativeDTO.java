package com.oncologic.clinic.dto.personal;

import com.oncologic.clinic.dto.personal.request.PersonalRequestDTO;
import lombok.Data;

@Data
public class AdministrativeDTO {
    private PersonalRequestDTO personalData;
    private String position;
    private String department;
}
