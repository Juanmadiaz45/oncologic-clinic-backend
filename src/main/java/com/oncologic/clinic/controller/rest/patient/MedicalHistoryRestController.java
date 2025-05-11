package com.oncologic.clinic.controller.rest.patient;

import com.oncologic.clinic.dto.patient.request.MedicalHistoryRequestDTO;
import com.oncologic.clinic.dto.patient.response.MedicalHistoryResponseDTO;
import com.oncologic.clinic.dto.patient.update.MedicalHistoryUpdateDTO;
import com.oncologic.clinic.service.patient.MedicalHistoryService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patients/medical-history")
public class MedicalHistoryRestController {
    private final MedicalHistoryService medicalHistoryService;

    public MedicalHistoryRestController(MedicalHistoryService medicalHistoryService) {
        this.medicalHistoryService = medicalHistoryService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<MedicalHistoryResponseDTO> getMedicalHistoryById(@PathVariable Long id) {
        try {
            MedicalHistoryResponseDTO response = medicalHistoryService.getMedicalHistoryById(id);
            return ResponseEntity.ok(response); // 200 OK
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // 404 Not Found
        }
    }

    @GetMapping
    public ResponseEntity<List<MedicalHistoryResponseDTO>> getAllMedicalHistories() {
        List<MedicalHistoryResponseDTO> histories = medicalHistoryService.getAllMedicalHistories();
        if (histories.isEmpty()) {
            return ResponseEntity.noContent().build(); // 204 No Content
        }
        return ResponseEntity.ok(histories); // 200 OK
    }

    @PostMapping
    public ResponseEntity<MedicalHistoryResponseDTO> createMedicalHistory(@Valid @RequestBody MedicalHistoryRequestDTO dto) {
        try {
            MedicalHistoryResponseDTO created = medicalHistoryService.createMedicalHistory(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(created); // 201 Created
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // 404 si el paciente no existe
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null); // 400 Bad Request
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<MedicalHistoryResponseDTO> updateMedicalHistory(
            @PathVariable Long id,
            @Valid @RequestBody MedicalHistoryUpdateDTO dto
    ) {
        try {
            MedicalHistoryResponseDTO updated = medicalHistoryService.updateMedicalHistory(id, dto);
            return ResponseEntity.ok(updated); // 200 OK
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // 404 Not Found
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMedicalHistory(@PathVariable Long id) {
        try {
            medicalHistoryService.deleteMedicalHistory(id);
            return ResponseEntity.noContent().build(); // 204 No Content
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // 404 Not Found
        }
    }
}
