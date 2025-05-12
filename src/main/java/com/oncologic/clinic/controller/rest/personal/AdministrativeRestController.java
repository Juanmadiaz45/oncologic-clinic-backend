package com.oncologic.clinic.controller.rest.personal;

import com.oncologic.clinic.dto.personal.AdministrativeDTO;
import com.oncologic.clinic.dto.personal.response.AdministrativeResponseDTO;
import com.oncologic.clinic.service.personal.AdministrativeService;
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

@Tag(name = "Administrative Personnel", description = "Operations related to administrative staff management")
@RestController
@RequestMapping("/api/administratives")
@RequiredArgsConstructor
public class AdministrativeRestController {

    private final AdministrativeService administrativeService;

    @Operation(summary = "Create a new administrative staff member")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Administrative created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data")
    })
    @PostMapping
    public ResponseEntity<AdministrativeResponseDTO> createAdministrative(
            @Valid @RequestBody AdministrativeDTO administrativeDTO) {
        AdministrativeResponseDTO createdAdministrative = administrativeService.createAdministrative(administrativeDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdAdministrative);
    }

    @Operation(summary = "Get an administrative staff member by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Administrative found"),
            @ApiResponse(responseCode = "404", description = "Administrative not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<AdministrativeResponseDTO> getAdministrativeById(@PathVariable Long id) {
        return ResponseEntity.ok(administrativeService.getAdministrativeById(id));
    }

    @Operation(summary = "Get all administrative staff members")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of administratives retrieved")
    })
    @GetMapping
    public ResponseEntity<List<AdministrativeResponseDTO>> getAllAdministratives() {
        return ResponseEntity.ok(administrativeService.getAllAdministrative());
    }

    @Operation(summary = "Update an existing administrative staff member")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Administrative updated successfully"),
            @ApiResponse(responseCode = "404", description = "Administrative not found"),
            @ApiResponse(responseCode = "400", description = "Invalid request data")
    })
    @PutMapping("/{id}")
    public ResponseEntity<AdministrativeResponseDTO> updateAdministrative(
            @PathVariable Long id,
            @Valid @RequestBody AdministrativeDTO administrativeDTO) {
        return ResponseEntity.ok(administrativeService.updateAdministrative(id, administrativeDTO));
    }

    @Operation(summary = "Delete an administrative staff member")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Administrative deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Administrative not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAdministrative(@PathVariable Long id) {
        administrativeService.deleteAdministrative(id);
        return ResponseEntity.noContent().build();
    }
}