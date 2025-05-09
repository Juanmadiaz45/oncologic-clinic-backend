package com.oncologic.clinic.dto.patient.request;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AppointmentResultRequestDTO {
    private LocalDateTime evaluationDate;
    private Long medicalHistoryId;
}
