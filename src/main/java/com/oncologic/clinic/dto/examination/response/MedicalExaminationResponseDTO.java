package com.oncologic.clinic.dto.examination.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MedicalExaminationResponseDTO {
    private String id;
    private LocalDateTime dateOfRealization;
    private Long laboratoryId;
    private Long typeOfExamId;
    private Long medicalHistoryId;
}