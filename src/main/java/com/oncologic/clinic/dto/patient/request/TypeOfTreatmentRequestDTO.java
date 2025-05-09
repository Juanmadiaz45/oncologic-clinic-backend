package com.oncologic.clinic.dto.patient.request;

import lombok.Data;

@Data
public class TypeOfTreatmentRequestDTO {
    private String name;
    private String description;
    private Long treatmentId;
}
