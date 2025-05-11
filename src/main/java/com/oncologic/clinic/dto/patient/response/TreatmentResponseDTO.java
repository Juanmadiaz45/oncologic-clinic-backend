package com.oncologic.clinic.dto.patient.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TreatmentResponseDTO {
    private Long id;
    private String name;
    private String description;
    private LocalDateTime dateStart;
    private LocalDateTime endDate;
    private Long appointmentResultId;
    private List<Long> typeOfTreatmentIds;
    private List<Long> prescribedMedicineIds;
    private List<Long> medicalAppointmentIds;
}
