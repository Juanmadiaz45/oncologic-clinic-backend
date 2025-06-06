package com.oncologic.clinic.dto.appointment.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MedicalOfficeResponseDTO {
    private Long id;
    private String name;
    private String location;
    private String equipment;
    private List<Long> medicalAppointmentIds;
}
