package com.oncologic.clinic.dto.personal.response;

import com.oncologic.clinic.dto.SpecialityDTO;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DoctorResponseDTO {
    private PersonalResponseDTO personalData;
    private String medicalLicenseNumber;
    private Set<SpecialityDTO> specialities;
}
