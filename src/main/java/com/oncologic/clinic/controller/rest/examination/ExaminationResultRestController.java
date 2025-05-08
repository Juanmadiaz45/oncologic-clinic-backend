package com.oncologic.clinic.controller.rest.examination;

import com.oncologic.clinic.dto.examination.request.ExaminationResultRequestDTO;
import com.oncologic.clinic.dto.examination.response.ExaminationResultResponseDTO;
import com.oncologic.clinic.dto.examination.update.ExaminationResultUpdateDTO;
import com.oncologic.clinic.service.examination.ExaminationResultService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/examination-results")
@RequiredArgsConstructor
public class ExaminationResultRestController {

    private final ExaminationResultService examinationResultService;

    @GetMapping("/{id}")
    public ResponseEntity<ExaminationResultResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(examinationResultService.getExaminationResultById(id));
    }

    @GetMapping
    public ResponseEntity<List<ExaminationResultResponseDTO>> getAll() {
        return ResponseEntity.ok(examinationResultService.getAllExaminationResults());
    }

    @PostMapping
    public ResponseEntity<ExaminationResultResponseDTO> create(
            @Valid @RequestBody ExaminationResultRequestDTO requestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(examinationResultService.createExaminationResult(requestDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ExaminationResultResponseDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody ExaminationResultUpdateDTO updateDTO) {
        return ResponseEntity.ok(examinationResultService.updateExaminationResult(id, updateDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        examinationResultService.deleteExaminationResult(id);
        return ResponseEntity.noContent().build();
    }
}