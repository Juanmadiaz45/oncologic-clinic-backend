package com.oncologic.clinic.dto.personal.response;

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
    private Long id;
    private String medicalLicenseNumber;
    private Set<Long> specialityIds; // Doctor's specialty IDs
    private PersonalResponseDTO personalData;
}