package com.oncologic.clinic.dto.patient.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ObservationResponseDTO {
    private Long id;
    private String content;
    private String recommendation;
    private Long appointmentResultId;
}
