package com.oncologic.clinic.dto.patient.request;

import lombok.Data;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MedicalHistoryRequestDTO {
    private Long patientId;
    private LocalDateTime creationDate;
    private String currentHealthStatus;
}
