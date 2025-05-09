package com.oncologic.clinic.dto.patient.response;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
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
