package com.oncologic.clinic.dto.patient.update;

import lombok.Data;

@Data
public class TypeOfTreatmentUpdateDTO {
    private String name;
    private String description;
    private Long treatmentId;
}
