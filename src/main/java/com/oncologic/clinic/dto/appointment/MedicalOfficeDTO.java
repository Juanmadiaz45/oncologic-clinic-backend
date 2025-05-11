package com.oncologic.clinic.dto.appointment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MedicalOfficeDTO {
    private String name;
    private Long medicalAppointmentId;
}
