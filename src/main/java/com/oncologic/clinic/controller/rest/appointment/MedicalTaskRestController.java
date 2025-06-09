package com.oncologic.clinic.controller.rest.appointment;

import com.oncologic.clinic.dto.appointment.MedicalTaskDTO;
import com.oncologic.clinic.dto.appointment.response.MedicalTaskResponseDTO;
import com.oncologic.clinic.service.appointment.MedicalTaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Medical Tasks", description = "Operations related to medical task management")
@RestController
@RequestMapping("/api/medical-tasks")
@RequiredArgsConstructor
public class MedicalTaskRestController {

    private final MedicalTaskService service;

    @Operation(summary = "Get a medical task by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Medical task found"),
            @ApiResponse(responseCode = "404", description = "Medical task not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<MedicalTaskResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getMedicalTaskById(id));
    }

    @Operation(summary = "Get all medical tasks")
    @ApiResponse(responseCode = "200", description = "List of all medical tasks")
    @GetMapping
    public ResponseEntity<List<MedicalTaskResponseDTO>> getAll() {
        return ResponseEntity.ok(service.getAllMedicalTasks());
    }

    @Operation(summary = "Create a new medical task")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Medical task created"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PostMapping
    public ResponseEntity<MedicalTaskResponseDTO> create(
            @Valid @RequestBody MedicalTaskDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.createMedicalTask(dto));
    }

    @Operation(summary = "Update an existing medical task")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Medical task updated"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Medical task not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<MedicalTaskResponseDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody MedicalTaskDTO dto) {
        return ResponseEntity.ok(service.updateMedicalTask(id, dto));
    }

    @Operation(summary = "Delete a medical task")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Medical task deleted"),
            @ApiResponse(responseCode = "404", description = "Medical task not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteMedicalTask(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get medical tasks by IDs")
    @ApiResponse(responseCode = "200", description = "List of medical tasks")
    @GetMapping("/by-ids")
    public ResponseEntity<List<MedicalTaskResponseDTO>> getByIds(
            @RequestParam @NonNull List<Long> ids) {
        return ResponseEntity.ok(service.getTasksByIds(ids));
    }

}
