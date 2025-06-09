package com.oncologic.clinic.controller.rest.availability;

import com.oncologic.clinic.dto.availability.AvailabilityDTO;
import com.oncologic.clinic.dto.availability.response.AvailabilityResponseDTO;
import com.oncologic.clinic.service.availability.AvailabilityService;
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

@Tag(name = "Availability", description = "Endpoints for managing availabilities")
@RestController
@RequestMapping("/api/availabilities")
@RequiredArgsConstructor
public class AvailabilityRestController {

    private final AvailabilityService availabilityService;

    @Operation(summary = "Create an availability")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Availability successfully created"),
            @ApiResponse(responseCode = "400", description = "Invalid data")
    })
    @PostMapping
    public ResponseEntity<AvailabilityResponseDTO> createAvailability(
            @Valid @RequestBody AvailabilityDTO requestDTO) {
        AvailabilityResponseDTO createdAvailability = availabilityService.createAvailability(requestDTO);
        System.out.println(createdAvailability);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdAvailability);
    }

    @Operation(summary = "Get an availability by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Availability found"),
            @ApiResponse(responseCode = "404", description = "Availability not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<AvailabilityResponseDTO> getAvailabilityById(@PathVariable Long id) {
        return ResponseEntity.ok(availabilityService.getAvailabilityById(id));
    }

    @Operation(summary = "Get all availabilities")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of availabilities")
    })
    @GetMapping
    public ResponseEntity<List<AvailabilityResponseDTO>> getAllAvailabilities() {
        return ResponseEntity.ok(availabilityService.getAllAvailabilities());
    }

    @Operation(summary = "Update an availability")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Availability successfully updated"),
            @ApiResponse(responseCode = "400", description = "Invalid data"),
            @ApiResponse(responseCode = "404", description = "Availability not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<AvailabilityResponseDTO> updateAvailability(
            @PathVariable Long id,
            @Valid @RequestBody AvailabilityDTO updateDTO) {
        return ResponseEntity.ok(availabilityService.updateAvailability(id, updateDTO));
    }

    @Operation(summary = "Delete an availability")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Availability successfully deleted"),
            @ApiResponse(responseCode = "404", description = "Availability not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAvailability(@PathVariable Long id) {
        availabilityService.deleteAvailability(id);
        return ResponseEntity.noContent().build();
    }
}