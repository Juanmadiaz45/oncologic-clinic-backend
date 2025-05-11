package com.oncologic.clinic.dto.patient.response;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentResultResponseDTO {
    private Long id;
    private LocalDateTime evaluationDate;
    private Long medicalHistoryId;
    private List<Long> observationIds;
    private List<Long> treatmentIds;
}
