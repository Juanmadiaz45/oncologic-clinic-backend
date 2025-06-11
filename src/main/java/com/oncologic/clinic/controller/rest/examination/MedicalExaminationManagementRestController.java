package com.oncologic.clinic.controller.rest.examination;

import com.oncologic.clinic.dto.examination.request.MedicalExaminationRequestDTO;
import com.oncologic.clinic.dto.examination.response.MedicalExaminationResponseDTO;
import com.oncologic.clinic.dto.examination.update.MedicalExaminationUpdateDTO;
import com.oncologic.clinic.service.examination.MedicalExaminationManagementService;
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
import java.util.Map;

@Tag(name = "Medical Examination Management", description = "Endpoints for doctors and admins to manage medical examinations")
@RestController
@RequestMapping("/api/medical-examination-management")
@RequiredArgsConstructor
public class MedicalExaminationManagementRestController {
    private final MedicalExaminationManagementService service;

    @Operation(summary = "Create medical examination for patient")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Medical examination created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Patient not found")
    })
    @PostMapping("/patients/{patientId}/examinations")
    public ResponseEntity<MedicalExaminationResponseDTO> createExaminationForPatient(
            @PathVariable Long patientId,
            @Valid @RequestBody MedicalExaminationRequestDTO requestDTO) {
        MedicalExaminationResponseDTO examination = service.createExaminationForPatient(patientId, requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(examination);
    }

    @Operation(summary = "Get all examinations for a patient")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of patient examinations"),
            @ApiResponse(responseCode = "404", description = "Patient not found")
    })
    @GetMapping("/patients/{patientId}/examinations")
    public ResponseEntity<List<MedicalExaminationResponseDTO>> getPatientExaminations(
            @PathVariable Long patientId) {
        List<MedicalExaminationResponseDTO> examinations = service.getExaminationsByPatientId(patientId);
        return ResponseEntity.ok(examinations);
    }

    @Operation(summary = "Get examination details with results")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Examination details with results"),
            @ApiResponse(responseCode = "404", description = "Examination not found")
    })
    @GetMapping("/examinations/{examinationId}/details")
    public ResponseEntity<Map<String, Object>> getExaminationWithResults(
            @PathVariable String examinationId) {
        Map<String, Object> details = service.getExaminationDetailsWithResults(examinationId);
        return ResponseEntity.ok(details);
    }

    @Operation(summary = "Update medical examination")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Examination updated successfully"),
            @ApiResponse(responseCode = "404", description = "Examination not found"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PutMapping("/examinations/{examinationId}")
    public ResponseEntity<MedicalExaminationResponseDTO> updateExamination(
            @PathVariable String examinationId,
            @Valid @RequestBody MedicalExaminationUpdateDTO updateDTO) {
        MedicalExaminationResponseDTO examination = service.updateExamination(examinationId, updateDTO);
        return ResponseEntity.ok(examination);
    }

    @Operation(summary = "Delete medical examination")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Examination deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Examination not found")
    })
    @DeleteMapping("/examinations/{examinationId}")
    public ResponseEntity<Void> deleteExamination(@PathVariable String examinationId) {
        service.deleteExamination(examinationId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get patient medical history with examinations and results")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Complete medical history with examinations"),
            @ApiResponse(responseCode = "404", description = "Patient not found")
    })
    @GetMapping("/patients/{patientId}/medical-history/complete")
    public ResponseEntity<Map<String, Object>> getCompleteMedicalHistory(
            @PathVariable Long patientId) {
        Map<String, Object> completeHistory = service.getCompleteMedicalHistoryWithExaminations(patientId);
        return ResponseEntity.ok(completeHistory);
    }
}
