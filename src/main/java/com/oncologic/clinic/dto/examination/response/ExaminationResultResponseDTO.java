package com.oncologic.clinic.dto.examination.response;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExaminationResultResponseDTO {
    private Long id;
    private LocalDateTime generationDate;
    private Long medicalHistoryId;
}