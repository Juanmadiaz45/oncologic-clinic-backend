package com.oncologic.clinic.dto.examination.request;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MedicalExaminationRequestDTO {
    private String id;
    private LocalDateTime dateOfRealization;
    private Long laboratoryId;
    private Long typeOfExamId;
    private Long medicalHistoryId;
}