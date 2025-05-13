package com.oncologic.clinic.controller.rest.patient;

import com.oncologic.clinic.dto.patient.request.TreatmentRequestDTO;
import com.oncologic.clinic.dto.patient.response.TreatmentResponseDTO;
import com.oncologic.clinic.dto.patient.update.TreatmentUpdateDTO;
import com.oncologic.clinic.service.patient.TreatmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Treatments", description = "Operations related to patient treatments")
@RestController
@RequestMapping("/api/treatments")
public class TreatmentRestController {
    private final TreatmentService treatmentService;

    public TreatmentRestController(TreatmentService treatmentService) {
        this.treatmentService = treatmentService;
    }

    @Operation(summary = "Get treatment by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Treatment found"),
            @ApiResponse(responseCode = "404", description = "Treatment not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<TreatmentResponseDTO> getById(@PathVariable Long id) {
        TreatmentResponseDTO response = treatmentService.getTreatmentById(id);
        return ResponseEntity.ok(response); // 200 OK
    }

    @Operation(summary = "Get all treatments")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of treatments")
    })
    @GetMapping
    public ResponseEntity<List<TreatmentResponseDTO>> getAll() {
        List<TreatmentResponseDTO> response = treatmentService.getAllTreatments();
        return ResponseEntity.ok(response); // 200 OK
    }

    @Operation(summary = "Create a new treatment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Treatment created"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PostMapping
    public ResponseEntity<TreatmentResponseDTO> create(@Valid @RequestBody TreatmentRequestDTO dto) {
        TreatmentResponseDTO created = treatmentService.createTreatment(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created); // 201 Created
    }

    @Operation(summary = "Update an existing treatment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Treatment updated"),
            @ApiResponse(responseCode = "404", description = "Treatment not found"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PutMapping("/{id}")
    public ResponseEntity<TreatmentResponseDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody TreatmentUpdateDTO dto) {
        TreatmentResponseDTO updated = treatmentService.updateTreatment(id, dto);
        return ResponseEntity.ok(updated); // 200 OK
    }

    @Operation(summary = "Delete a treatment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Treatment deleted"),
            @ApiResponse(responseCode = "404", description = "Treatment not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        treatmentService.deleteTreatment(id);
        return ResponseEntity.noContent().build(); // 204 No Content
    }
}
