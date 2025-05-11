package com.oncologic.clinic.dto.patient.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PrescribedMedicineRequestDTO {
    private String medicine;
    private LocalDateTime prescriptionDate;
    private String instructions;
    private String dose;
    private Integer duration;
    private String frequencyOfAdministration;
    private Long treatmentId;
}
