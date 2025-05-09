package com.oncologic.clinic.dto.patient.response;

import lombok.Data;

@Data
public class TypeOfTreatmentResponseDTO {
    private Long id;
    private String name;
    private String description;
    private Long treatmentId;
}
