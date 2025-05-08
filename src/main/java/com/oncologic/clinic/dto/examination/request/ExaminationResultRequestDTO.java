package com.oncologic.clinic.dto.examination.request;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ExaminationResultRequestDTO {
    private LocalDateTime generationDate;
    private byte[] resultsReport;
    private Long medicalHistoryId;
}