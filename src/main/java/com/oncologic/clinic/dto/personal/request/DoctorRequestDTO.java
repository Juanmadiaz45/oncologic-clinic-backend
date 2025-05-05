package com.oncologic.clinic.dto.personal.request;

import lombok.Data;
import java.util.Set;

@Data
public class DoctorRequestDTO extends PersonalRequestDTO {
    private String medicalLicenseNumber;
    private Set<Long> specialityIds;
}
