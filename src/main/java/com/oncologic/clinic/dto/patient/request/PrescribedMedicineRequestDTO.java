package com.oncologic.clinic.dto.patient.request;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PrescribedMedicineRequestDTO {
    private String medicine;
    private LocalDateTime prescriptionDate;
    private String instructions;
    private String dose;
    private Integer duration;
    private String frequencyOfAdministration;
    private Long treatmentId;
}
