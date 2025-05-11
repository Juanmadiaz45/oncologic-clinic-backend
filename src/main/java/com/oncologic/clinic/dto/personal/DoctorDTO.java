package com.oncologic.clinic.dto.personal;

import com.oncologic.clinic.dto.personal.request.PersonalRequestDTO;
import lombok.Data;
import java.util.Set;

@Data
public class DoctorDTO {
    private PersonalRequestDTO personalData;
    private String medicalLicenseNumber;
    private Set<Long> specialityIds;
}
