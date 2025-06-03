package com.oncologic.clinic.dto.personal;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DoctorDTO {

    @Valid
    private PersonalDTO personalData; // Nested personal and user data

    @NotBlank(message = "The medical license number is mandatory")
    @Size(max = 50, message = "The medical license number cannot exceed 50 characters.")
    private String medicalLicenseNumber;

    private Set<Long> specialityIds; // Specialty IDs to be assigned
}