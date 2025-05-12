package com.oncologic.clinic.dto.registration;

import lombok.*;

import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class RegisterDoctorDTO extends RegisterPersonalDTO {
    private String medicalLicenseNumber;
    private Set<Long> specialityIds;
}
