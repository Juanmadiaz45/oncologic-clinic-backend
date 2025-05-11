package com.oncologic.clinic.controller.rest.patient;

import com.oncologic.clinic.dto.patient.request.TreatmentRequestDTO;
import com.oncologic.clinic.dto.patient.response.TreatmentResponseDTO;
import com.oncologic.clinic.dto.patient.update.TreatmentUpdateDTO;
import com.oncologic.clinic.service.patient.TreatmentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/treatments")
public class TreatmentRestController {
    private final TreatmentService treatmentService;

    public TreatmentRestController(TreatmentService treatmentService) {
        this.treatmentService = treatmentService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<TreatmentResponseDTO> getById(@PathVariable Long id) {
        TreatmentResponseDTO response = treatmentService.getTreatmentById(id);
        return ResponseEntity.ok(response); // 200 OK
    }

    @GetMapping
    public ResponseEntity<List<TreatmentResponseDTO>> getAll() {
        List<TreatmentResponseDTO> response = treatmentService.getAllTreatments();
        return ResponseEntity.ok(response); // 200 OK
    }

    @PostMapping
    public ResponseEntity<TreatmentResponseDTO> create(@Valid @RequestBody TreatmentRequestDTO dto) {
        TreatmentResponseDTO created = treatmentService.createTreatment(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created); // 201 Created
    }

    @PutMapping("/{id}")
    public ResponseEntity<TreatmentResponseDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody TreatmentUpdateDTO dto) {
        TreatmentResponseDTO updated = treatmentService.updateTreatment(id, dto);
        return ResponseEntity.ok(updated); // 200 OK
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        treatmentService.deleteTreatment(id);
        return ResponseEntity.noContent().build(); // 204 No Content
    }
}
