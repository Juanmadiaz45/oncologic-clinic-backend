package com.oncologic.clinic.controller.rest.examination;

import com.oncologic.clinic.dto.examination.LaboratoryDTO;
import com.oncologic.clinic.dto.examination.response.LaboratoryResponseDTO;
import com.oncologic.clinic.service.examination.LaboratoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/laboratories")
@RequiredArgsConstructor
public class LaboratoryRestController {

    private final LaboratoryService laboratoryService;

    @GetMapping("/{id}")
    public ResponseEntity<LaboratoryResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(laboratoryService.getLaboratoryById(id));
    }

    @GetMapping
    public ResponseEntity<List<LaboratoryResponseDTO>> getAll() {
        return ResponseEntity.ok(laboratoryService.getAllLaboratories());
    }

    @PostMapping
    public ResponseEntity<LaboratoryResponseDTO> create(
            @Valid @RequestBody LaboratoryDTO requestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(laboratoryService.createLaboratory(requestDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<LaboratoryResponseDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody LaboratoryDTO updateDTO) {
        return ResponseEntity.ok(laboratoryService.updateLaboratory(id, updateDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        laboratoryService.deleteLaboratory(id);
        return ResponseEntity.noContent().build();
    }
}