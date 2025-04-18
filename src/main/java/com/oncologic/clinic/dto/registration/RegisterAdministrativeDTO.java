package com.oncologic.clinic.dto.registration;

import lombok.Data;

@Data
public class RegisterAdministrativeDTO extends RegisterPersonalDTO{
    private String position;
    private String department;
}
