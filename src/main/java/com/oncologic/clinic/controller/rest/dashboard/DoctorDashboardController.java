package com.oncologic.clinic.controller.rest.dashboard;

import com.oncologic.clinic.dto.dashboard.doctor_dashboard.AppointmentSummaryDTO;
import com.oncologic.clinic.dto.dashboard.doctor_dashboard.DoctorDashboardMetricsDTO;
import com.oncologic.clinic.dto.dashboard.doctor_dashboard.NextAppointmentDTO;
import com.oncologic.clinic.exception.runtime.dashboard.DashboardAccessException;
import com.oncologic.clinic.exception.runtime.dashboard.UserNotDoctorException;
import com.oncologic.clinic.exception.runtime.personal.DoctorNotFoundException;
import com.oncologic.clinic.exception.runtime.user.UserNotFoundException;
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
public class DoctorDashboardController {

    private final DoctorDashboardService doctorDashboardService;

    @Autowired
    public DoctorDashboardController(DoctorDashboardService doctorDashboardService) {
        this.doctorDashboardService = doctorDashboardService;
    }

    @Operation(summary = "Get dashboard metrics for current doctor")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Metrics retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "User is not a doctor"),
            @ApiResponse(responseCode = "404", description = "User or doctor not found"),
            @ApiResponse(responseCode = "503", description = "Dashboard service temporarily unavailable")
    })
    @GetMapping("/metrics")
    public ResponseEntity<DoctorDashboardMetricsDTO> getDashboardMetrics(Authentication authentication) {
        String username = authentication.getName();
        log.debug("Getting dashboard metrics for doctor: {}", username);

        try {
            DoctorDashboardMetricsDTO metrics = doctorDashboardService.getDashboardMetrics(username);

            // Si el servicio devuelve null por alguna razón de negocio válida, crear métricas por defecto
            if (metrics == null) {
                metrics = createDefaultMetrics();
                log.info("No metrics found for doctor {}, returning defaults", username);
            }

            log.debug("Dashboard metrics retrieved successfully for doctor: {}", username);
            return ResponseEntity.ok(metrics);

        } catch (UserNotFoundException | UserNotDoctorException | DoctorNotFoundException e) {
            // ✅ CORRECCIÓN: Estas excepciones deben propagarse al GlobalExceptionHandler
            // NO capturarlas aquí, dejar que el GlobalExceptionHandler las maneje
            log.debug("Authorization/validation error for user {}: {}", username, e.getMessage());
            throw e; // Relanzar para que las maneje el GlobalExceptionHandler

        } catch (DashboardAccessException e) {
            // ✅ CORRECCIÓN: Esta excepción también debe propagarse
            log.debug("Dashboard access error for user {}: {}", username, e.getMessage());
            throw e; // Relanzar para que las maneje el GlobalExceptionHandler

        } catch (Exception e) {
            // ✅ CORRECCIÓN: Solo capturar excepciones técnicas inesperadas
            // y convertirlas en DashboardAccessException para manejo consistente
            log.error("Unexpected technical error getting dashboard metrics for user: {}", username, e);
            throw new DashboardAccessException("Unable to retrieve dashboard metrics due to technical issues", e);
        }
    }

    @Operation(summary = "Get today's appointments for current doctor")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Today's appointments retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "User is not a doctor"),
            @ApiResponse(responseCode = "404", description = "User or doctor not found"),
            @ApiResponse(responseCode = "503", description = "Dashboard service temporarily unavailable")
    })
    @GetMapping("/appointments/today")
    public ResponseEntity<List<AppointmentSummaryDTO>> getTodayAppointments(Authentication authentication) {
        String username = authentication.getName();
        log.debug("Getting today's appointments for doctor: {}", username);

        try {
            List<AppointmentSummaryDTO> appointments = doctorDashboardService.getTodayAppointments(username, LocalDate.now());

            // Si el servicio devuelve null por alguna razón de negocio válida, crear lista vacía
            if (appointments == null) {
                appointments = new ArrayList<>();
                log.info("No appointments found for doctor {} today, returning empty list", username);
            }

            log.debug("Retrieved {} appointments for doctor: {}", appointments.size(), username);
            return ResponseEntity.ok(appointments);

        } catch (UserNotFoundException | UserNotDoctorException | DoctorNotFoundException e) {
            // ✅ CORRECCIÓN: Propagar excepciones de autorización/validación
            log.debug("Authorization/validation error for user {}: {}", username, e.getMessage());
            throw e;

        } catch (DashboardAccessException e) {
            // ✅ CORRECCIÓN: Propagar excepciones de acceso al dashboard
            log.debug("Dashboard access error for user {}: {}", username, e.getMessage());
            throw e;

        } catch (Exception e) {
            // ✅ CORRECCIÓN: Solo capturar excepciones técnicas inesperadas
            log.error("Unexpected technical error getting today's appointments for user: {}", username, e);
            throw new DashboardAccessException("Unable to retrieve today's appointments due to technical issues", e);
        }
    }

    @Operation(summary = "Get next appointment for current doctor")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Next appointment retrieved successfully"),
            @ApiResponse(responseCode = "204", description = "No upcoming appointments found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "User is not a doctor"),
            @ApiResponse(responseCode = "404", description = "User or doctor not found"),
            @ApiResponse(responseCode = "503", description = "Dashboard service temporarily unavailable")
    })
    @GetMapping("/appointments/next")
    public ResponseEntity<NextAppointmentDTO> getNextAppointment(Authentication authentication) {
        String username = authentication.getName();
        log.debug("Getting next appointment for doctor: {}", username);

        try {
            NextAppointmentDTO nextAppointment = doctorDashboardService.getNextAppointment(username);

            if (nextAppointment == null) {
                log.debug("No next appointment found for doctor: {}", username);
                return ResponseEntity.noContent().build(); // 204 No Content es apropiado aquí
            }

            log.debug("Next appointment retrieved for doctor: {}", username);
            return ResponseEntity.ok(nextAppointment);

        } catch (UserNotFoundException | UserNotDoctorException | DoctorNotFoundException e) {
            // ✅ CORRECCIÓN: Propagar excepciones de autorización/validación
            log.debug("Authorization/validation error for user {}: {}", username, e.getMessage());
            throw e;

        } catch (DashboardAccessException e) {
            // ✅ CORRECCIÓN: Propagar excepciones de acceso al dashboard
            log.debug("Dashboard access error for user {}: {}", username, e.getMessage());
            throw e;

        } catch (Exception e) {
            // ✅ CORRECCIÓN: Solo capturar excepciones técnicas inesperadas
            log.error("Unexpected technical error getting next appointment for user: {}", username, e);
            throw new DashboardAccessException("Unable to retrieve next appointment due to technical issues", e);
        }
    }

    /**
     * Crea métricas por defecto cuando no hay datos disponibles por razones de negocio válidas
     * (NO para errores de autorización - esos deben manejarse como excepciones)
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