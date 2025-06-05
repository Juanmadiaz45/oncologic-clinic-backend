package com.oncologic.clinic.service.dashboard.doctor_dashboard.interfaces;

import com.oncologic.clinic.dto.dashboard.doctor_dashboard.AppointmentSummaryDTO;
import com.oncologic.clinic.dto.dashboard.doctor_dashboard.DoctorDashboardMetricsDTO;
import com.oncologic.clinic.dto.dashboard.doctor_dashboard.NextAppointmentDTO;

import java.time.LocalDate;
import java.util.List;

public interface DoctorDashboardService {
    DoctorDashboardMetricsDTO getDashboardMetrics(String username);
    List<AppointmentSummaryDTO> getTodayAppointments(String username, LocalDate date);
    NextAppointmentDTO getNextAppointment(String username);
}
