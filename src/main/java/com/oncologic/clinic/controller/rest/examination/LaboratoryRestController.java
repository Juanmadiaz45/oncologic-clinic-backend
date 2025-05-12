package com.oncologic.clinic.controller.rest.examination;

import com.oncologic.clinic.dto.examination.LaboratoryDTO;
import com.oncologic.clinic.dto.examination.response.LaboratoryResponseDTO;
import com.oncologic.clinic.service.examination.LaboratoryService;
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

@Tag(name = "Laboratories", description = "Endpoints for managing laboratories")
@RestController
@RequestMapping("/api/laboratories")
@RequiredArgsConstructor
public class LaboratoryRestController {

    private final LaboratoryService laboratoryService;

    @Operation(summary = "Get a laboratory by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Laboratory found"),
            @ApiResponse(responseCode = "404", description = "Laboratory not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<LaboratoryResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(laboratoryService.getLaboratoryById(id));
    }

    @Operation(summary = "Get all laboratories")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of all laboratories")
    })
    @GetMapping
    public ResponseEntity<List<LaboratoryResponseDTO>> getAll() {
        return ResponseEntity.ok(laboratoryService.getAllLaboratories());
    }

    @Operation(summary = "Create a new laboratory")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Laboratory successfully created"),
            @ApiResponse(responseCode = "400", description = "Invalid data")
    })
    @PostMapping
    public ResponseEntity<LaboratoryResponseDTO> create(
            @Valid @RequestBody LaboratoryDTO requestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(laboratoryService.createLaboratory(requestDTO));
    }

    @Operation(summary = "Update an existing laboratory")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Laboratory successfully updated"),
            @ApiResponse(responseCode = "400", description = "Invalid data"),
            @ApiResponse(responseCode = "404", description = "Laboratory not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<LaboratoryResponseDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody LaboratoryDTO updateDTO) {
        return ResponseEntity.ok(laboratoryService.updateLaboratory(id, updateDTO));
    }

    @Operation(summary = "Delete a laboratory")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Laboratory successfully deleted"),
            @ApiResponse(responseCode = "404", description = "Laboratory not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        laboratoryService.deleteLaboratory(id);
        return ResponseEntity.noContent().build();
    }
}