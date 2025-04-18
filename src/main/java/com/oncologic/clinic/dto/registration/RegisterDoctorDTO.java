package com.oncologic.clinic.dto.registration;

import lombok.Data;

@Data
public class RegisterDoctorDTO extends RegisterPersonalDTO{
    private String medicalLicenseNumber;

    private Long selectedSpecialityId;
    private String newSpecialityName;
    private String newSpecialityDescription;
}
