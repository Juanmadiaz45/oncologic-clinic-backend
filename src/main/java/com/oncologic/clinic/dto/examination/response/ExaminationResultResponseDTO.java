package com.oncologic.clinic.dto.examination.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ExaminationResultResponseDTO {
    private Long id;
    private LocalDateTime generationDate;
    private Long medicalHistoryId;
}