package com.oncologic.clinic.dto.appointment.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TypeOfMedicalAppointmentResponseDTO {
    private Long id;
    private String name;
}
