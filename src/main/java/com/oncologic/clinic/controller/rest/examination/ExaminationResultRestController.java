package com.oncologic.clinic.controller.rest.examination;

import com.oncologic.clinic.dto.examination.request.ExaminationResultRequestDTO;
import com.oncologic.clinic.dto.examination.response.ExaminationResultResponseDTO;
import com.oncologic.clinic.dto.examination.update.ExaminationResultUpdateDTO;
import com.oncologic.clinic.entity.examination.ExaminationResult;
import com.oncologic.clinic.entity.patient.MedicalHistory;
import com.oncologic.clinic.service.examination.ExaminationResultService;
import com.oncologic.clinic.service.examination.MedicalReportGenerator;
import com.oncologic.clinic.service.examination.impl.ExaminationResultServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Tag(name = "Examination Results", description = "Endpoints for managing examination results")
@RestController
@RequestMapping("/api/examination-results")
@RequiredArgsConstructor
public class ExaminationResultRestController {

    private final ExaminationResultService examinationResultService;
    private final ExaminationResultServiceImpl examinationResultServiceImpl; // Para métodos adicionales
    private final MedicalReportGenerator reportGenerator;

    @Operation(summary = "Get an examination result by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Examination result found"),
            @ApiResponse(responseCode = "404", description = "Examination result not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ExaminationResultResponseDTO> getById(@PathVariable Long id) {
        log.info("GET /examination-results/{}", id);
        return ResponseEntity.ok(examinationResultService.getExaminationResultById(id));
    }

    @Operation(summary = "Get all examination results")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of all examination results")
    })
    @GetMapping
    public ResponseEntity<List<ExaminationResultResponseDTO>> getAll() {
        log.info("GET /examination-results");
        return ResponseEntity.ok(examinationResultService.getAllExaminationResults());
    }

    @Operation(summary = "Get examination results by medical history ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of examination results for the patient")
    })
    @GetMapping("/by-medical-history/{medicalHistoryId}")
    public ResponseEntity<List<ExaminationResultResponseDTO>> getByMedicalHistoryId(
            @PathVariable Long medicalHistoryId) {
        log.info("GET /examination-results/by-medical-history/{}", medicalHistoryId);
        return ResponseEntity.ok(examinationResultService.getExaminationResultsByMedicalHistoryId(medicalHistoryId));
    }

    @Operation(summary = "Create a new examination result")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Examination result successfully created"),
            @ApiResponse(responseCode = "400", description = "Invalid data"),
            @ApiResponse(responseCode = "409", description = "Result already exists for this examination")
    })
    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody ExaminationResultRequestDTO requestDTO) {
        log.info("POST /examination-results for medical history ID: {}", requestDTO.getMedicalHistoryId());

        try {
            // Verificar si ya existe un resultado (opcional)
            if (examinationResultServiceImpl.hasResultForMedicalHistory(requestDTO.getMedicalHistoryId())) {
                log.warn("Result already exists for medical history ID: {}", requestDTO.getMedicalHistoryId());
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(Map.of(
                                "error", "RESULT_ALREADY_EXISTS",
                                "message", "Ya existe un resultado para este examen",
                                "medicalHistoryId", requestDTO.getMedicalHistoryId()
                        ));
            }

            ExaminationResultResponseDTO result = examinationResultService.createExaminationResult(requestDTO);
            log.info("Successfully created examination result with ID: {}", result.getId());

            return ResponseEntity.status(HttpStatus.CREATED).body(result);

        } catch (DataIntegrityViolationException e) {
            log.error("Data integrity violation: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of(
                            "error", "DATA_INTEGRITY_VIOLATION",
                            "message", "Error de integridad de datos: " + e.getMessage()
                    ));
        } catch (IllegalArgumentException e) {
            log.error("Invalid argument: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of(
                            "error", "INVALID_DATA",
                            "message", e.getMessage()
                    ));
        } catch (Exception e) {
            log.error("Unexpected error creating examination result: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "error", "INTERNAL_ERROR",
                            "message", "Error interno del servidor"
                    ));
        }
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
        log.info("PUT /examination-results/{}", id);
        return ResponseEntity.ok(examinationResultService.updateExaminationResult(id, updateDTO));
    }

    @Operation(summary = "Delete an examination result")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Examination result successfully deleted"),
            @ApiResponse(responseCode = "404", description = "Examination result not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("DELETE /examination-results/{}", id);
        examinationResultService.deleteExaminationResult(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Check if medical history has examination results")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Check completed")
    })
    @GetMapping("/exists/medical-history/{medicalHistoryId}")
    public ResponseEntity<Map<String, Object>> hasResultForMedicalHistory(
            @PathVariable Long medicalHistoryId) {
        log.info("GET /examination-results/exists/medical-history/{}", medicalHistoryId);

        boolean hasResult = examinationResultServiceImpl.hasResultForMedicalHistory(medicalHistoryId);

        return ResponseEntity.ok(Map.of(
                "medicalHistoryId", medicalHistoryId,
                "hasResult", hasResult
        ));
    }

    @Operation(summary = "Download examination result report as base64")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Report downloaded successfully"),
            @ApiResponse(responseCode = "404", description = "Result or report not found")
    })
    @GetMapping("/{id}/report")
    public ResponseEntity<Map<String, String>> downloadReport(@PathVariable Long id) {
        log.info("GET /examination-results/{}/report", id);

        try {
            String reportBase64 = examinationResultServiceImpl.getResultReportAsBase64(id);

            if (reportBase64 == null) {
                return ResponseEntity.notFound().build();
            }

            return ResponseEntity.ok(Map.of(
                    "resultId", id.toString(),
                    "reportBase64", reportBase64,
                    "filename", "reporte_examen_" + id + ".txt"
            ));

        } catch (Exception e) {
            log.error("Error downloading report for result ID {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al descargar el reporte"));
        }
    }

    @Operation(summary = "Generate automatic report for examination result")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Report generated successfully"),
            @ApiResponse(responseCode = "404", description = "Examination result not found")
    })
    @PostMapping("/{id}/generate-report")
    public ResponseEntity<Map<String, String>> generateAutomaticReport(
            @PathVariable Long id,
            @RequestParam(required = false) String examType) {

        try {
            ExaminationResult result = examinationResultService.getExaminationResultEntityById(id);

            // Obtener información del contexto
            String type = examType != null ? examType : getExamTypeFromResult(result);
            String laboratoryName = getLaboratoryNameFromResult(result);
            String patientName = result.getMedicalHistory().getPatient().getName();

            // Generar reporte específico usando el generador
            String generatedReport = reportGenerator.generateReport(type, laboratoryName, patientName);

            // Actualizar el resultado con el nuevo reporte
            result.setResultsReport(generatedReport);
            examinationResultService.createExaminationResult(result);

            Map<String, String> response = new HashMap<>();
            response.put("message", "Reporte generado exitosamente");
            response.put("reportType", type);
            response.put("reportSize", String.valueOf(generatedReport.length()));

            log.info("Generated {} report for examination result ID: {}", type, id);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("Error generating report for result ID {}: {}", id, e.getMessage());
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Error generando reporte: " + e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    // Métodos auxiliares para obtener información del contexto
    private String getExamTypeFromResult(ExaminationResult result) {
        // Buscar el último examen médico relacionado
        MedicalHistory medicalHistory = result.getMedicalHistory();
        if (medicalHistory.getMedicalExaminations() != null && !medicalHistory.getMedicalExaminations().isEmpty()) {
            return medicalHistory.getMedicalExaminations()
                    .stream()
                    .max((e1, e2) -> e1.getDateOfRealization().compareTo(e2.getDateOfRealization()))
                    .map(exam -> exam.getTypeOfExam() != null ? exam.getTypeOfExam().getName() : "General")
                    .orElse("General");
        }
        return "General";
    }

    private String getLaboratoryNameFromResult(ExaminationResult result) {
        // Buscar el laboratorio del último examen
        MedicalHistory medicalHistory = result.getMedicalHistory();
        if (medicalHistory.getMedicalExaminations() != null && !medicalHistory.getMedicalExaminations().isEmpty()) {
            return medicalHistory.getMedicalExaminations()
                    .stream()
                    .max((e1, e2) -> e1.getDateOfRealization().compareTo(e2.getDateOfRealization()))
                    .map(exam -> exam.getLaboratory() != null ? exam.getLaboratory().getName() : "Laboratorio Clínico")
                    .orElse("Laboratorio Clínico");
        }
        return "Laboratorio Clínico";
    }

}