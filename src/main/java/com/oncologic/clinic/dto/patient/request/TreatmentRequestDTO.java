package com.oncologic.clinic.dto.patient.request;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TreatmentRequestDTO {
    private String name;
    private String description;
    private LocalDateTime dateStart;
    private LocalDateTime endDate;
    private Long appointmentResultId;
}
