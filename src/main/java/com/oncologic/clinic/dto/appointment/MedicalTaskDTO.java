package com.oncologic.clinic.dto.appointment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MedicalTaskDTO {
    private String description;
    private Long estimatedTime;
    private String status;
    private String responsible;
    private Set<Long> medicalAppointmentIds;
}
