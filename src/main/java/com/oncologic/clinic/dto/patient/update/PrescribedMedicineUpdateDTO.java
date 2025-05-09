package com.oncologic.clinic.dto.patient.update;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PrescribedMedicineUpdateDTO {
    private String medicine;
    private LocalDateTime prescriptionDate;
    private String instructions;
    private String dose;
    private Integer duration;
    private String frequencyOfAdministration;
}
