package com.oncologic.clinic.controller.rest.appointment;

import com.oncologic.clinic.dto.appointment.TypeOfMedicalAppointmentDTO;
import com.oncologic.clinic.dto.appointment.response.TypeOfMedicalAppointmentResponseDTO;
import com.oncologic.clinic.service.appointment.TypeOfMedicalAppointmentService;
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

@Tag(name = "Type of Medical Appointments", description = "Operations related to medical appointment types")
@RestController
@RequestMapping("/api/medical-appointment-types")
@RequiredArgsConstructor
public class TypeOfMedicalAppointmentRestController {

    private final TypeOfMedicalAppointmentService service;

    @Operation(summary = "Get a medical appointment type by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Type found"),
            @ApiResponse(responseCode = "404", description = "Type not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<TypeOfMedicalAppointmentResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getTypeOfMedicalAppointmentById(id));
    }

    @Operation(summary = "Get all medical appointment types")
    @ApiResponse(responseCode = "200", description = "List of all types returned")
    @GetMapping
    public ResponseEntity<List<TypeOfMedicalAppointmentResponseDTO>> getAll() {
        return ResponseEntity.ok(service.getAllTypesOfMedicalAppointment());
    }

    @Operation(summary = "Create a new medical appointment type")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Type created"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PostMapping
    public ResponseEntity<TypeOfMedicalAppointmentResponseDTO> create(
            @Valid @RequestBody TypeOfMedicalAppointmentDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.createTypeOfMedicalAppointment(dto));
    }

    @Operation(summary = "Update an existing medical appointment type")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Type updated"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "404", description = "Type not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<TypeOfMedicalAppointmentResponseDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody TypeOfMedicalAppointmentDTO dto) {
        return ResponseEntity.ok(service.updateTypeOfMedicalAppointment(id, dto));
    }

    @Operation(summary = "Delete a medical appointment type")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Type deleted"),
            @ApiResponse(responseCode = "404", description = "Type not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteTypeOfMedicalAppointment(id);
        return ResponseEntity.noContent().build();
    }
}
