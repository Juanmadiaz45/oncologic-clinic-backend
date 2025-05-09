package com.oncologic.clinic.dto.patient.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PrescribedMedicineResponseDTO {
    private Long id;
    private String medicine;
    private LocalDateTime prescriptionDate;
    private String instructions;
    private String dose;
    private Integer duration;
    private String frequencyOfAdministration;
    private Long treatmentId;
}
