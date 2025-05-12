package com.oncologic.clinic.controller.rest.examination;

import com.oncologic.clinic.dto.examination.TypeOfExamDTO;
import com.oncologic.clinic.dto.examination.response.TypeOfExamResponseDTO;
import com.oncologic.clinic.service.examination.TypeOfExamService;
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

@Tag(name = "Types of Exam", description = "Endpoints for managing types of medical examinations")
@RestController
@RequestMapping("/api/types-of-exam")
@RequiredArgsConstructor
public class TypeOfExamRestController {

    private final TypeOfExamService typeOfExamService;

    @Operation(summary = "Get a type of exam by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Type of exam found"),
            @ApiResponse(responseCode = "404", description = "Type of exam not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<TypeOfExamResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(typeOfExamService.getTypeOfExamById(id));
    }

    @Operation(summary = "Get all types of exams")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of all types of exams")
    })
    @GetMapping
    public ResponseEntity<List<TypeOfExamResponseDTO>> getAll() {
        return ResponseEntity.ok(typeOfExamService.getAllTypesOfExam());
    }

    @Operation(summary = "Create a new type of exam")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Type of exam successfully created"),
            @ApiResponse(responseCode = "400", description = "Invalid data")
    })
    @PostMapping
    public ResponseEntity<TypeOfExamResponseDTO> create(
            @Valid @RequestBody TypeOfExamDTO requestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(typeOfExamService.createTypeOfExam(requestDTO));
    }

    @Operation(summary = "Update an existing type of exam")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Type of exam successfully updated"),
            @ApiResponse(responseCode = "400", description = "Invalid data"),
            @ApiResponse(responseCode = "404", description = "Type of exam not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<TypeOfExamResponseDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody TypeOfExamDTO updateDTO) {
        return ResponseEntity.ok(typeOfExamService.updateTypeOfExam(id, updateDTO));
    }

    @Operation(summary = "Delete a type of exam")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Type of exam successfully deleted"),
            @ApiResponse(responseCode = "404", description = "Type of exam not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        typeOfExamService.deleteTypeOfExam(id);
        return ResponseEntity.noContent().build();
    }
}