package com.oncologic.clinic.dto.patient.update;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AppointmentResultUpdateDTO {
    private LocalDateTime evaluationDate;
}
