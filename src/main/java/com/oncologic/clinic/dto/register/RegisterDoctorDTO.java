package com.oncologic.clinic.dto.register;

import lombok.Data;

@Data
public class RegisterDoctorDTO extends RegisterPersonalDTO{
    private String medicalLicenseNumber;
}
