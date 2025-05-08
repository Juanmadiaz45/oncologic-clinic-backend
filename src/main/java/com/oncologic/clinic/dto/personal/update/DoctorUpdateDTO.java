package com.oncologic.clinic.dto.personal.update;

import com.oncologic.clinic.dto.personal.request.PersonalRequestDTO;
import lombok.Data;

import java.util.Set;

@Data
public class DoctorUpdateDTO {
    private PersonalRequestDTO personalData;
    private String medicalLicenseNumber;
    private Set<Long> specialityIds;
}