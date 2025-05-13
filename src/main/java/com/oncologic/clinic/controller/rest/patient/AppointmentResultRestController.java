package com.oncologic.clinic.controller.rest.patient;

import com.oncologic.clinic.dto.patient.request.AppointmentResultRequestDTO;
import com.oncologic.clinic.dto.patient.response.AppointmentResultResponseDTO;
import com.oncologic.clinic.service.patient.AppointmentResultService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Tag(name = "Appointment Results", description = "Endpoints for managing appointment results")
@RestController
@RequestMapping("/api/appointment-results")
public class AppointmentResultRestController {
    private final AppointmentResultService appointmentResultService;

    public AppointmentResultRestController(AppointmentResultService appointmentResultService) {
        this.appointmentResultService = appointmentResultService;
    }

    @Operation(summary = "Get an appointment result by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Appointment result found"),
            @ApiResponse(responseCode = "404", description = "Appointment result not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<AppointmentResultResponseDTO> getById(@PathVariable Long id) {
        AppointmentResultResponseDTO result = appointmentResultService.getAppointmentResultById(id);
        return ResponseEntity.ok(result); // 200 OK
    }

    @Operation(summary = "Get all appointment results")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of all appointment results")
    })
    @GetMapping
    public ResponseEntity<List<AppointmentResultResponseDTO>> getAll() {
        List<AppointmentResultResponseDTO> results = appointmentResultService.getAllAppointmentResults();
        return ResponseEntity.ok(results); // 200 OK
    }

    @Operation(summary = "Create a new appointment result")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Appointment result successfully created"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PostMapping
    public ResponseEntity<AppointmentResultResponseDTO> create(@Valid @RequestBody AppointmentResultRequestDTO dto) {
        AppointmentResultResponseDTO created = appointmentResultService.createAppointmentResult(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created); // 201 Created
    }

    @Operation(summary = "Update an existing appointment result")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Appointment result successfully updated"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Appointment result not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<AppointmentResultResponseDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody AppointmentResultRequestDTO dto) {
        AppointmentResultResponseDTO updated = appointmentResultService.updateAppointmentResult(id, dto);
        return ResponseEntity.ok(updated); // 200 OK
    }

    @Operation(summary = "Delete an appointment result")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Appointment result successfully deleted"),
            @ApiResponse(responseCode = "404", description = "Appointment result not found"),
            @ApiResponse(responseCode = "409", description = "Conflict: Cannot delete because there are associated records")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        try {
            appointmentResultService.deleteAppointmentResult(id);
            return ResponseEntity.noContent().build();
        } catch (DataIntegrityViolationException ex) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Cannot be deleted because it has associated records", ex);
        }
    }
}
