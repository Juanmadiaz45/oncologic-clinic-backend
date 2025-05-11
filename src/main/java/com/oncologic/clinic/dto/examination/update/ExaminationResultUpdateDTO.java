package com.oncologic.clinic.dto.examination.update;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExaminationResultUpdateDTO {
    private LocalDateTime generationDate;
    private byte[] resultsReport;
}
