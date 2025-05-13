package com.oncologic.clinic.controller.rest.patient;

import com.oncologic.clinic.dto.patient.request.ObservationRequestDTO;
import com.oncologic.clinic.dto.patient.response.ObservationResponseDTO;
import com.oncologic.clinic.dto.patient.update.ObservationUpdateDTO;
import com.oncologic.clinic.service.patient.ObservationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Observations", description = "Endpoints for managing patient observations")
@RestController
@RequestMapping("/api/observations")
public class ObservationRestController {
    private final ObservationService observationService;

    public ObservationRestController(ObservationService observationService) {
        this.observationService = observationService;
    }

    @Operation(summary = "Get observation by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Observation found"),
            @ApiResponse(responseCode = "404", description = "Observation not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ObservationResponseDTO> getById(@PathVariable Long id) {
        ObservationResponseDTO observation = observationService.getObservationById(id);
        return ResponseEntity.ok(observation); // 200 OK
    }

    @Operation(summary = "Get all observations")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of all observations"),
            @ApiResponse(responseCode = "204", description = "No content")
    })
    @GetMapping
    public ResponseEntity<List<ObservationResponseDTO>> getAll() {
        List<ObservationResponseDTO> observations = observationService.getAllObservations();
        return ResponseEntity.ok(observations); // 200 OK
    }

    @Operation(summary = "Create a new observation")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Observation successfully created"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PostMapping
    public ResponseEntity<ObservationResponseDTO> create(@Valid @RequestBody ObservationRequestDTO dto) {
        ObservationResponseDTO created = observationService.createObservation(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created); // 201 Created
    }

    @Operation(summary = "Update an existing observation")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Observation successfully updated"),
            @ApiResponse(responseCode = "404", description = "Observation not found"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ObservationResponseDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody ObservationUpdateDTO dto) {
        ObservationResponseDTO updated = observationService.updateObservation(id, dto);
        return ResponseEntity.ok(updated); // 200 OK
    }

    @Operation(summary = "Delete an observation")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Observation successfully deleted"),
            @ApiResponse(responseCode = "404", description = "Observation not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        observationService.deleteObservation(id);
        return ResponseEntity.noContent().build(); // 204 No Content
    }
}
