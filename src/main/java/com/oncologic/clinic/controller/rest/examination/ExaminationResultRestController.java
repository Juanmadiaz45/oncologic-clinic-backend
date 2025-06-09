package com.oncologic.clinic.controller.rest.examination;

import com.oncologic.clinic.dto.examination.request.ExaminationResultRequestDTO;
import com.oncologic.clinic.dto.examination.response.ExaminationResultResponseDTO;
import com.oncologic.clinic.dto.examination.update.ExaminationResultUpdateDTO;
import com.oncologic.clinic.service.examination.ExaminationResultService;
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

@Tag(name = "Examination Results", description = "Endpoints for managing examination results")
@RestController
@RequestMapping("/api/examination-results")
@RequiredArgsConstructor
public class ExaminationResultRestController {

    private final ExaminationResultService examinationResultService;

    @Operation(summary = "Get an examination result by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Examination result found"),
            @ApiResponse(responseCode = "404", description = "Examination result not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ExaminationResultResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(examinationResultService.getExaminationResultById(id));
    }

    @Operation(summary = "Get all examination results")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of all examination results")
    })
    @GetMapping
    public ResponseEntity<List<ExaminationResultResponseDTO>> getAll() {
        return ResponseEntity.ok(examinationResultService.getAllExaminationResults());
    }

    @Operation(summary = "Get examination results by medical history ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of examination results for the patient")
    })
    @GetMapping("/by-medical-history/{medicalHistoryId}")
    public ResponseEntity<List<ExaminationResultResponseDTO>> getByMedicalHistoryId(@PathVariable Long medicalHistoryId) {
        return ResponseEntity.ok(examinationResultService.getExaminationResultsByMedicalHistoryId(medicalHistoryId));
    }

    @Operation(summary = "Create a new examination result")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Examination result successfully created"),
            @ApiResponse(responseCode = "400", description = "Invalid data")
    })
    @PostMapping
    public ResponseEntity<ExaminationResultResponseDTO> create(
            @Valid @RequestBody ExaminationResultRequestDTO requestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(examinationResultService.createExaminationResult(requestDTO));
    }

    @Operation(summary = "Update an existing examination result")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Examination result successfully updated"),
            @ApiResponse(responseCode = "400", description = "Invalid data"),
            @ApiResponse(responseCode = "404", description = "Examination result not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ExaminationResultResponseDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody ExaminationResultUpdateDTO updateDTO) {
        return ResponseEntity.ok(examinationResultService.updateExaminationResult(id, updateDTO));
    }

    @Operation(summary = "Delete an examination result")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Examination result successfully deleted"),
            @ApiResponse(responseCode = "404", description = "Examination result not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        examinationResultService.deleteExaminationResult(id);
        return ResponseEntity.noContent().build();
    }
}