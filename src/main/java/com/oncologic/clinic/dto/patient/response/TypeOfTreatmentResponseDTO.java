package com.oncologic.clinic.dto.patient.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TypeOfTreatmentResponseDTO {
    private Long id;
    private String name;
    private String description;
    private Long treatmentId;
}
