package com.oncologic.clinic.dto.examination.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MedicalExaminationRequestDTO {
    private String id;
    private LocalDateTime dateOfRealization;
    private Long laboratoryId;
    private Long typeOfExamId;
    private Long medicalHistoryId;
}