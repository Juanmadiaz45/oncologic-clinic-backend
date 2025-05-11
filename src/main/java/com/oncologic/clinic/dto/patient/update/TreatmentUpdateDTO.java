package com.oncologic.clinic.dto.patient.update;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TreatmentUpdateDTO {
    private String name;
    private String description;
    private LocalDateTime dateStart;
    private LocalDateTime endDate;
}
