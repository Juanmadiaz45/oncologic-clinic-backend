package com.oncologic.clinic.dto.appointment.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MedicalAppointmentResponseDTO {
    private Long id;
    private Long doctorId;
    private Long typeOfMedicalAppointmentId;
    private LocalDateTime appointmentDate;
    private Long treatmentId;
    private Long medicalHistoryId;
    private Long medicalOfficeId;
    private Set<Long> medicalTaskIds;
}

