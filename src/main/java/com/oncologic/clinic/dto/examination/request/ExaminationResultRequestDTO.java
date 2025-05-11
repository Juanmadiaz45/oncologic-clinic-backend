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
public class ExaminationResultRequestDTO {
    private LocalDateTime generationDate;
    private byte[] resultsReport;
    private Long medicalHistoryId;
}