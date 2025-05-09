package com.oncologic.clinic.dto.patient.update;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TreatmentUpdateDTO {
    private String name;
    private String description;
    private LocalDateTime dateStart;
    private LocalDateTime endDate;
}
