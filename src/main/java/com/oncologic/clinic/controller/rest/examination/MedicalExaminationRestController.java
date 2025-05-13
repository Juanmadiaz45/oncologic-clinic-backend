package com.oncologic.clinic.controller.rest.examination;

import com.oncologic.clinic.dto.examination.request.MedicalExaminationRequestDTO;
import com.oncologic.clinic.dto.examination.response.MedicalExaminationResponseDTO;
import com.oncologic.clinic.dto.examination.update.MedicalExaminationUpdateDTO;
import com.oncologic.clinic.service.examination.MedicalExaminationService;
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

@Tag(name = "Medical Examinations", description = "Endpoints for managing medical examinations")
@RestController
@RequestMapping("/api/medical-examinations")
@RequiredArgsConstructor
public class MedicalExaminationRestController {

    private final MedicalExaminationService medicalExaminationService;

    @Operation(summary = "Get a medical examination by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Medical examination found"),
            @ApiResponse(responseCode = "404", description = "Medical examination not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<MedicalExaminationResponseDTO> getById(@PathVariable String id) {
        return ResponseEntity.ok(medicalExaminationService.getMedicalExaminationById(id));
    }

    @Operation(summary = "Get all medical examinations")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of all medical examinations")
    })
    @GetMapping
    public ResponseEntity<List<MedicalExaminationResponseDTO>> getAll() {
        return ResponseEntity.ok(medicalExaminationService.getAllMedicalExaminations());
    }

    @Operation(summary = "Create a new medical examination")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Medical examination successfully created"),
            @ApiResponse(responseCode = "400", description = "Invalid data")
    })
    @PostMapping
    public ResponseEntity<MedicalExaminationResponseDTO> create(
            @Valid @RequestBody MedicalExaminationRequestDTO requestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(medicalExaminationService.createMedicalExamination(requestDTO));
    }

    @Operation(summary = "Update an existing medical examination")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Medical examination successfully updated"),
            @ApiResponse(responseCode = "400", description = "Invalid data"),
            @ApiResponse(responseCode = "404", description = "Medical examination not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<MedicalExaminationResponseDTO> update(
            @PathVariable String id,
            @Valid @RequestBody MedicalExaminationUpdateDTO updateDTO) {
        return ResponseEntity.ok(medicalExaminationService.updateMedicalExamination(id, updateDTO));
    }

    @Operation(summary = "Delete a medical examination")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Medical examination successfully deleted"),
            @ApiResponse(responseCode = "404", description = "Medical examination not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        medicalExaminationService.deleteMedicalExamination(id);
        return ResponseEntity.noContent().build();
    }
}