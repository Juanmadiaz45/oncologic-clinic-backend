package com.oncologic.clinic.controller.rest.patient;

import com.oncologic.clinic.dto.patient.request.TypeOfTreatmentRequestDTO;
import com.oncologic.clinic.dto.patient.response.TypeOfTreatmentResponseDTO;
import com.oncologic.clinic.service.patient.TypeOfTreatmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Type of Treatments", description = "Operations related to treatment types")
@RestController
@RequestMapping("/api/type-of-treatments")
public class TypeOfTreatmentRestController {
    private final TypeOfTreatmentService typeOfTreatmentService;

    public TypeOfTreatmentRestController(TypeOfTreatmentService typeOfTreatmentService) {
        this.typeOfTreatmentService = typeOfTreatmentService;
    }

    @Operation(summary = "Get type of treatment by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Type of treatment found"),
            @ApiResponse(responseCode = "404", description = "Type of treatment not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<TypeOfTreatmentResponseDTO> getById(@PathVariable Long id) {
        TypeOfTreatmentResponseDTO response = typeOfTreatmentService.getTypeOfTreatmentById(id);
        return ResponseEntity.ok(response); // 200 OK
    }

    @Operation(summary = "Get all types of treatment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of treatment types")
    })
    @GetMapping
    public ResponseEntity<List<TypeOfTreatmentResponseDTO>> getAll() {
        List<TypeOfTreatmentResponseDTO> response = typeOfTreatmentService.getAllTypesOfTreatment();
        return ResponseEntity.ok(response); // 200 OK
    }

    @Operation(summary = "Create a new type of treatment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Type of treatment created"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PostMapping
    public ResponseEntity<TypeOfTreatmentResponseDTO> create(@Valid @RequestBody TypeOfTreatmentRequestDTO dto) {
        TypeOfTreatmentResponseDTO created = typeOfTreatmentService.createTypeOfTreatment(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created); // 201 Created
    }

    @Operation(summary = "Update an existing type of treatment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Type of treatment updated"),
            @ApiResponse(responseCode = "404", description = "Type of treatment not found"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PutMapping("/{id}")
    public ResponseEntity<TypeOfTreatmentResponseDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody TypeOfTreatmentRequestDTO dto) {
        TypeOfTreatmentResponseDTO updated = typeOfTreatmentService.updateTypeOfTreatment(id, dto);
        return ResponseEntity.ok(updated); // 200 OK
    }

    @Operation(summary = "Delete a type of treatment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Type of treatment deleted"),
            @ApiResponse(responseCode = "404", description = "Type of treatment not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        typeOfTreatmentService.deleteTypeOfTreatment(id);
        return ResponseEntity.noContent().build(); // 204 No Content
    }
}
