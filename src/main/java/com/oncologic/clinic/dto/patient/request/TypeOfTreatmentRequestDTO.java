package com.oncologic.clinic.dto.patient.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TypeOfTreatmentRequestDTO {
    private String name;
    private String description;
    private Long treatmentId;
}
