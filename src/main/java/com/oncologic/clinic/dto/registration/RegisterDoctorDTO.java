package com.oncologic.clinic.dto.registration;

import lombok.Data;

import java.util.Set;

@Data
public class RegisterDoctorDTO extends RegisterPersonalDTO{
    private String medicalLicenseNumber;
    private Set<Long> specialityIds;
}
