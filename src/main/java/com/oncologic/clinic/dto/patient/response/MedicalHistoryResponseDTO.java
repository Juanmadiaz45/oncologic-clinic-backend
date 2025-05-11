package com.oncologic.clinic.dto.patient.response;

import com.oncologic.clinic.dto.appointment.response.MedicalAppointmentResponseDTO;

import java.time.LocalDateTime;
import java.util.List;

import com.oncologic.clinic.dto.examination.response.ExaminationResultResponseDTO;
import com.oncologic.clinic.dto.examination.response.MedicalExaminationResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class MedicalHistoryResponseDTO {
    private Long id;
    private Long patientId;
    private LocalDateTime creationDate;
    private String currentHealthStatus;
    private List<MedicalAppointmentResponseDTO> medicalAppointments;
    private List<MedicalExaminationResponseDTO> medicalExaminations;
    private List<AppointmentResultResponseDTO> appointmentResults;
    private List<ExaminationResultResponseDTO> examinationResults;
}
