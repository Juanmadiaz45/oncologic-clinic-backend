package com.oncologic.clinic.controller.rest.patient;

import com.oncologic.clinic.dto.examination.response.ExaminationResultResponseDTO;
import com.oncologic.clinic.dto.examination.response.MedicalExaminationResponseDTO;
import com.oncologic.clinic.service.examination.ExaminationResultService;
import com.oncologic.clinic.service.examination.MedicalExaminationService;
import com.oncologic.clinic.service.patient.PatientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Patient Medical Data", description = "Endpoints for patients to view their medical examinations and results")
@RestController
@RequestMapping("/api/patient-medical")
@RequiredArgsConstructor
public class PatientMedicalRestController {
    private final MedicalExaminationService medicalExaminationService;
    private final ExaminationResultService examinationResultService;
    private final PatientService patientService;

    @Operation(summary = "Get patient's medical examinations by medical history ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of patient's medical examinations"),
            @ApiResponse(responseCode = "404", description = "Medical history not found")
    })
    @GetMapping("/examinations/by-history/{medicalHistoryId}")
    public ResponseEntity<List<MedicalExaminationResponseDTO>> getPatientExaminationsByHistory(@PathVariable Long medicalHistoryId) {
        List<MedicalExaminationResponseDTO> examinations = medicalExaminationService.getMedicalExaminationsByMedicalHistoryId(medicalHistoryId);
        return ResponseEntity.ok(examinations);
    }

    @Operation(summary = "Get patient's medical examinations by patient ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of patient's medical examinations"),
            @ApiResponse(responseCode = "404", description = "Patient or medical history not found")
    })
    @GetMapping("/examinations/by-patient/{patientId}")
    public ResponseEntity<List<MedicalExaminationResponseDTO>> getPatientExaminationsByPatientId(@PathVariable Long patientId) {
        Long medicalHistoryId = patientService.getMedicalHistoryIdByPatientId(patientId);
        List<MedicalExaminationResponseDTO> examinations = medicalExaminationService.getMedicalExaminationsByMedicalHistoryId(medicalHistoryId);
        return ResponseEntity.ok(examinations);
    }

    @Operation(summary = "Get patient's examination results by medical history ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of patient's examination results"),
            @ApiResponse(responseCode = "404", description = "Medical history not found")
    })
    @GetMapping("/examination-results/by-history/{medicalHistoryId}")
    public ResponseEntity<List<ExaminationResultResponseDTO>> getPatientExaminationResultsByHistory(@PathVariable Long medicalHistoryId) {
        List<ExaminationResultResponseDTO> results = examinationResultService.getExaminationResultsByMedicalHistoryId(medicalHistoryId);
        return ResponseEntity.ok(results);
    }

    @Operation(summary = "Get patient's examination results by patient ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of patient's examination results"),
            @ApiResponse(responseCode = "404", description = "Patient or medical history not found")
    })
    @GetMapping("/examination-results/by-patient/{patientId}")
    public ResponseEntity<List<ExaminationResultResponseDTO>> getPatientExaminationResultsByPatientId(@PathVariable Long patientId) {
        Long medicalHistoryId = patientService.getMedicalHistoryIdByPatientId(patientId);
        List<ExaminationResultResponseDTO> results = examinationResultService.getExaminationResultsByMedicalHistoryId(medicalHistoryId);
        return ResponseEntity.ok(results);
    }

    @Operation(summary = "Get specific examination result by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Examination result found"),
            @ApiResponse(responseCode = "404", description = "Examination result not found")
    })
    @GetMapping("/examination-result/{resultId}")
    public ResponseEntity<ExaminationResultResponseDTO> getExaminationResultById(@PathVariable Long resultId) {
        ExaminationResultResponseDTO result = examinationResultService.getExaminationResultById(resultId);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "Get specific medical examination by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Medical examination found"),
            @ApiResponse(responseCode = "404", description = "Medical examination not found")
    })
    @GetMapping("/examination/{examinationId}")
    public ResponseEntity<MedicalExaminationResponseDTO> getMedicalExaminationById(@PathVariable String examinationId) {
        MedicalExaminationResponseDTO examination = medicalExaminationService.getMedicalExaminationById(examinationId);
        return ResponseEntity.ok(examination);
    }
}
