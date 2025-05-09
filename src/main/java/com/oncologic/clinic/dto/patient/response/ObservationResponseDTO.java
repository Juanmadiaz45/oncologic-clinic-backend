package com.oncologic.clinic.dto.patient.response;

import lombok.Data;

@Data
public class ObservationResponseDTO {
    private Long id;
    private String content;
    private String recommendation;
    private Long appointmentResultId;
}
