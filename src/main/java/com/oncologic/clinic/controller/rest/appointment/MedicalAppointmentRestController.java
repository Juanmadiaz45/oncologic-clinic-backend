package com.oncologic.clinic.controller.rest.appointment;

import com.oncologic.clinic.dto.appointment.MedicalAppointmentDTO;
import com.oncologic.clinic.dto.appointment.response.MedicalAppointmentResponseDTO;
import com.oncologic.clinic.service.appointment.MedicalAppointmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Medical Appointments", description = "Operations related to medical appointments")
@RestController
@RequestMapping("/api/medical-appointments")
@RequiredArgsConstructor
public class MedicalAppointmentRestController {

    private final MedicalAppointmentService service;

    @Operation(summary = "Get a medical appointment by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Medical appointment found"),
            @ApiResponse(responseCode = "404", description = "Medical appointment not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<MedicalAppointmentResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getMedicalAppointmentById(id));
    }

    @Operation(summary = "Get all medical appointments")
    @ApiResponse(responseCode = "200", description = "List of all medical appointments")
    @GetMapping
    public ResponseEntity<List<MedicalAppointmentResponseDTO>> getAll() {
        return ResponseEntity.ok(service.getAllMedicalAppointments());
    }

    @Operation(summary = "Create a new medical appointment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Medical appointment created"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PostMapping
    public ResponseEntity<MedicalAppointmentResponseDTO> create(@Valid @RequestBody MedicalAppointmentDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createMedicalAppointment(dto));
    }

    @Operation(summary = "Update an existing medical appointment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Medical appointment updated"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Medical appointment not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<MedicalAppointmentResponseDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody MedicalAppointmentDTO dto) {
        return ResponseEntity.ok(service.updateMedicalAppointment(id, dto));
    }

    @Operation(summary = "Delete a medical appointment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Medical appointment deleted"),
            @ApiResponse(responseCode = "404", description = "Medical appointment not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteMedicalAppointment(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get base medical appointments (templates)")
    @ApiResponse(responseCode = "200", description = "List of base medical appointments")
    @GetMapping("/base")
    public ResponseEntity<List<MedicalAppointmentResponseDTO>> getBaseAppointments() {
        return ResponseEntity.ok(service.getBaseAppointments());
    }

}
