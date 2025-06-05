package com.oncologic.clinic.controller.rest.dashboard;

import com.oncologic.clinic.dto.dashboard.doctor_dashboard.AppointmentSummaryDTO;
import com.oncologic.clinic.dto.dashboard.doctor_dashboard.DoctorDashboardMetricsDTO;
import com.oncologic.clinic.dto.dashboard.doctor_dashboard.NextAppointmentDTO;
import com.oncologic.clinic.service.dashboard.doctor_dashboard.interfaces.DoctorDashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.Authentication;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/doctor-dashboard")
public class DoctorDashboardController {
    @Autowired
    private DoctorDashboardService doctorDashboardService;

    @Operation(summary = "Get dashboard metrics for current doctor")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Metrics retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @GetMapping("/metrics")
    public ResponseEntity<DoctorDashboardMetricsDTO> getDashboardMetrics(Authentication authentication) {
        String username = authentication.getName();
        DoctorDashboardMetricsDTO metrics = doctorDashboardService.getDashboardMetrics(username);
        return ResponseEntity.ok(metrics);
    }

    @Operation(summary = "Get today's appointments for current doctor")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Today's appointments retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @GetMapping("/appointments/today")
    public ResponseEntity<List<AppointmentSummaryDTO>> getTodayAppointments(Authentication authentication) {
        String username = authentication.getName();
        List<AppointmentSummaryDTO> appointments = doctorDashboardService.getTodayAppointments(username, LocalDate.now());
        return ResponseEntity.ok(appointments);
    }

    @Operation(summary = "Get next appointment for current doctor")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Next appointment retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "No upcoming appointments found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @GetMapping("/appointments/next")
    public ResponseEntity<NextAppointmentDTO> getNextAppointment(Authentication authentication) {
        String username = authentication.getName();
        NextAppointmentDTO nextAppointment = doctorDashboardService.getNextAppointment(username);

        if (nextAppointment == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(nextAppointment);
    }
}
