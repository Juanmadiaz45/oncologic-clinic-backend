package com.oncologic.clinic.dto.personal.update;

import lombok.Data;

import java.util.Set;

@Data
public class DoctorUpdateDTO extends PersonalUpdateDTO {
    private String medicalLicenseNumber;
    private Set<Long> specialityIds;
}