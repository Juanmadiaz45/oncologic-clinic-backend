package com.oncologic.clinic.dto.patient.response;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MedicalHistoryResponseDTO {
    private Long id;
    private Long patientId;
    private LocalDateTime creationDate;
    private String currentHealthStatus;

    // IDs only - better performance
    private List<Long> medicalAppointmentIds;
    private List<String> medicalExaminationIds;
    private List<Long> appointmentResultIds;
    private List<Long> examinationResultIds;
}