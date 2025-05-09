package com.oncologic.clinic.dto.patient.request;

import lombok.Data;

@Data
public class ObservationRequestDTO {
    private String content;
    private String recommendation;
    private Long appointmentResultId;
}
