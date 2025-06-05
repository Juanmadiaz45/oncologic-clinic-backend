package com.oncologic.clinic.dto.dashboard.doctor_dashboard;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DoctorDashboardMetricsDTO {
    private Integer appointmentsToday;
    private Integer activePatients;
    private Integer pendingObservations;
    private String currentDate;
    private String currentDay;
}
