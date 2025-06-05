package com.oncologic.clinic.dto.dashboard.doctor_dashboard;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentSummaryDTO {
    private Long id;
    private String time;
    private String patientName;
    private String appointmentType;
    private String status;
    private String office;
}
