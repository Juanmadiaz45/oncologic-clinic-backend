package com.oncologic.clinic.dto.patient.update;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MedicalHistoryUpdateDTO {
    private String currentHealthStatus;
}
