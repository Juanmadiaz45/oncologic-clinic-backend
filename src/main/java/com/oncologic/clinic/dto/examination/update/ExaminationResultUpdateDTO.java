package com.oncologic.clinic.dto.examination.update;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ExaminationResultUpdateDTO {
    private LocalDateTime generationDate;
    private byte[] resultsReport;
}
