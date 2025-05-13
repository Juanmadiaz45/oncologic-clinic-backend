package com.oncologic.clinic.controller.rest.availability;

import com.oncologic.clinic.dto.availability.StatusDTO;
import com.oncologic.clinic.dto.availability.response.StatusResponseDTO;
import com.oncologic.clinic.service.availability.StatusService;
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

@Tag(name = "Status", description = "Endpoints for managing statuses")
@RestController
@RequestMapping("/api/statuses")
@RequiredArgsConstructor
public class StatusRestController {

    private final StatusService statusService;

    @Operation(summary = "Create a new status")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Status successfully created"),
            @ApiResponse(responseCode = "400", description = "Invalid data")
    })
    @PostMapping
    public ResponseEntity<StatusResponseDTO> createStatus(
            @Valid @RequestBody StatusDTO requestDTO) {
        StatusResponseDTO createdStatus = statusService.createStatus(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdStatus);
    }

    @Operation(summary = "Get a status by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Status found"),
            @ApiResponse(responseCode = "404", description = "Status not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<StatusResponseDTO> getStatusById(@PathVariable Long id) {
        return ResponseEntity.ok(statusService.getStatusById(id));
    }

    @Operation(summary = "Get all statuses")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of statuses")
    })
    @GetMapping
    public ResponseEntity<List<StatusResponseDTO>> getAllStatuses() {
        return ResponseEntity.ok(statusService.getAllStatuses());
    }

    @Operation(summary = "Update a status")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Status successfully updated"),
            @ApiResponse(responseCode = "400", description = "Invalid data"),
            @ApiResponse(responseCode = "404", description = "Status not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<StatusResponseDTO> updateStatus(
            @PathVariable Long id,
            @Valid @RequestBody StatusDTO updateDTO) {
        return ResponseEntity.ok(statusService.updateStatus(id, updateDTO));
    }

    @Operation(summary = "Delete a status")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Status successfully deleted"),
            @ApiResponse(responseCode = "404", description = "Status not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStatus(@PathVariable Long id) {
        statusService.deleteStatus(id);
        return ResponseEntity.noContent().build();
    }
}