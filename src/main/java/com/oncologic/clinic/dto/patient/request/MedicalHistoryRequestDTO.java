package com.oncologic.clinic.dto.patient.request;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MedicalHistoryRequestDTO {
    private Long patientId;
    private LocalDateTime creationDate;
    private String currentHealthStatus;
}
