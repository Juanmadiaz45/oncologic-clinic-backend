package com.oncologic.clinic.dto.registration;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class RegisterDoctorDTO extends RegisterPersonalDTO {
    private String medicalLicenseNumber;

    private Long selectedSpecialityId;
    private String newSpecialityName;
    private String newSpecialityDescription;
}
