package com.oncologic.clinic.dto.examination.request;

import com.fasterxml.jackson.annotation.JsonProperty;
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

    @JsonProperty("resultsReportBase64")
    private String resultsReportBase64;

    private Long medicalHistoryId;
}