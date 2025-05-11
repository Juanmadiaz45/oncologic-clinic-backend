package com.oncologic.clinic.dto.patient.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ObservationRequestDTO {
    private String content;
    private String recommendation;
    private Long appointmentResultId;
}
