package com.oncologic.clinic.dto.patient.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TreatmentRequestDTO {
    private String name;
    private String description;
    private LocalDateTime dateStart;
    private LocalDateTime endDate;
    private Long appointmentResultId;
}
