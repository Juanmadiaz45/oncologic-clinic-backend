package com.oncologic.clinic.controller.rest.dashboard;

import com.oncologic.clinic.dto.dashboard.doctor_dashboard.AppointmentSummaryDTO;
import com.oncologic.clinic.dto.dashboard.doctor_dashboard.DoctorDashboardMetricsDTO;
import com.oncologic.clinic.dto.dashboard.doctor_dashboard.NextAppointmentDTO;
import com.oncologic.clinic.service.dashboard.doctor_dashboard.interfaces.DoctorDashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Slf4j
@RestController
@RequestMapping("/api/doctor-dashboard")
@CrossOrigin(origins = "http://localhost:3000")
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
        try {
            String username = authentication.getName();
            log.info("Getting dashboard metrics for doctor: {}", username);

            DoctorDashboardMetricsDTO metrics = doctorDashboardService.getDashboardMetrics(username);

            // Si el servicio devuelve null, crear métricas por defecto
            if (metrics == null) {
                metrics = createDefaultMetrics();
                log.info("No metrics found for doctor {}, returning defaults", username);
            }

            return ResponseEntity.ok(metrics);

        } catch (Exception e) {
            log.warn("Error getting dashboard metrics, returning default values: ", e);

            // En lugar de error, devolver valores por defecto
            DoctorDashboardMetricsDTO defaultMetrics = createDefaultMetrics();
            return ResponseEntity.ok(defaultMetrics);
        }
    }

    @Operation(summary = "Get today's appointments for current doctor")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Today's appointments retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @GetMapping("/appointments/today")
    public ResponseEntity<List<AppointmentSummaryDTO>> getTodayAppointments(Authentication authentication) {
        try {
            String username = authentication.getName();
            log.info("Getting today's appointments for doctor: {}", username);

            List<AppointmentSummaryDTO> appointments = doctorDashboardService.getTodayAppointments(username, LocalDate.now());

            // Si el servicio devuelve null, crear lista vacía
            if (appointments == null) {
                appointments = new ArrayList<>();
                log.info("No appointments found for doctor {} today, returning empty list", username);
            }

            return ResponseEntity.ok(appointments);

        } catch (Exception e) {
            log.warn("Error getting today's appointments, returning empty list: ", e);

            // En lugar de error, devolver lista vacía
            return ResponseEntity.ok(new ArrayList<>());
        }
    }

    @Operation(summary = "Get next appointment for current doctor")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Next appointment retrieved successfully"),
            @ApiResponse(responseCode = "204", description = "No upcoming appointments found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @GetMapping("/appointments/next")
    public ResponseEntity<NextAppointmentDTO> getNextAppointment(Authentication authentication) {
        try {
            String username = authentication.getName();
            log.info("Getting next appointment for doctor: {}", username);

            NextAppointmentDTO nextAppointment = doctorDashboardService.getNextAppointment(username);

            if (nextAppointment == null) {
                log.info("No next appointment found for doctor: {}", username);
                // Cambiar 404 por 204 No Content (es más semánticamente correcto)
                return ResponseEntity.noContent().build();
            }

            return ResponseEntity.ok(nextAppointment);

        } catch (Exception e) {
            log.info("Error getting next appointment for doctor, returning no content: ", e);

            // En lugar de error, devolver 204 No Content
            return ResponseEntity.noContent().build();
        }
    }

    /**
     * Crea métricas por defecto cuando no hay datos disponibles
     */
    private DoctorDashboardMetricsDTO createDefaultMetrics() {
        LocalDate now = LocalDate.now();

        return DoctorDashboardMetricsDTO.builder()
                .appointmentsToday(0)
                .activePatients(0)
                .pendingObservations(0)
                .currentDate(now.format(DateTimeFormatter.ofPattern("dd")))
                .currentDay(now.format(DateTimeFormatter.ofPattern("EEEE", new Locale("es", "ES"))))
                .build();
    }
}