package com.oncologic.clinic.controller.rest.patient;

import com.oncologic.clinic.dto.patient.request.PatientRequestDTO;
import com.oncologic.clinic.dto.patient.response.PatientResponseDTO;
import com.oncologic.clinic.dto.patient.update.PatientUpdateDTO;
import com.oncologic.clinic.service.patient.PatientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Patients", description = "Endpoints for managing patient records")
@RestController
@RequestMapping("/api/users/patients")
public class PatientRestController {

    private final PatientService patientService;

    public PatientRestController(PatientService patientService) {
        this.patientService = patientService;
    }

    @Operation(summary = "Get patient by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Patient found"),
            @ApiResponse(responseCode = "404", description = "Patient not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<PatientResponseDTO> getPatientById(@PathVariable Long id) {
        try {
            PatientResponseDTO response = patientService.getPatientById(id);
            return ResponseEntity.ok(response); // 200 OK
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // 404 Not Found
        }
    }

    @Operation(summary = "Get all patients")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of all patients"),
            @ApiResponse(responseCode = "204", description = "No content")
    })
    @GetMapping
    public ResponseEntity<List<PatientResponseDTO>> getAllPatients() {
        List<PatientResponseDTO> patients = patientService.getAllPatients();
        if (patients.isEmpty()) {
            return ResponseEntity.noContent().build(); // 204 No Content si no hay pacientes
        }
        return ResponseEntity.ok(patients); // 200 OK
    }

    @Operation(summary = "Create a new patient")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Patient successfully created"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PostMapping
    public ResponseEntity<PatientResponseDTO> createPatient(@Valid @RequestBody PatientRequestDTO patientDTO) {
        try {
            PatientResponseDTO createdPatient = patientService.createPatient(patientDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdPatient); // 201 Created
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build(); // 400 Bad Request en caso de error inesperado
        }
    }

    @Operation(summary = "Update an existing patient")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Patient successfully updated"),
            @ApiResponse(responseCode = "404", description = "Patient not found"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PutMapping("/{id}")
    public ResponseEntity<PatientResponseDTO> updatePatient(@PathVariable Long id, @Valid @RequestBody PatientUpdateDTO patientDTO) {
        try {
            PatientResponseDTO updatedPatient = patientService.updatePatient(id, patientDTO);
            return ResponseEntity.ok(updatedPatient); // 200 OK
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // 404 Not Found
        }
    }

    @Operation(summary = "Delete a patient")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Patient successfully deleted"),
            @ApiResponse(responseCode = "404", description = "Patient not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePatient(@PathVariable Long id) {
        try {
            patientService.deletePatient(id);
            return ResponseEntity.noContent().build(); // 204 No Content
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // 404 Not Found
        }
    }
}
