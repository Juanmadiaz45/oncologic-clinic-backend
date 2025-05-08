package com.oncologic.clinic.controller.rest.examination;

import com.oncologic.clinic.dto.examination.request.MedicalExaminationRequestDTO;
import com.oncologic.clinic.dto.examination.response.MedicalExaminationResponseDTO;
import com.oncologic.clinic.dto.examination.update.MedicalExaminationUpdateDTO;
import com.oncologic.clinic.service.examination.MedicalExaminationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/medical-examinations")
@RequiredArgsConstructor
public class MedicalExaminationRestController {

    private final MedicalExaminationService medicalExaminationService;

    @GetMapping("/{id}")
    public ResponseEntity<MedicalExaminationResponseDTO> getById(@PathVariable String id) {
        return ResponseEntity.ok(medicalExaminationService.getMedicalExaminationById(id));
    }

    @GetMapping
    public ResponseEntity<List<MedicalExaminationResponseDTO>> getAll() {
        return ResponseEntity.ok(medicalExaminationService.getAllMedicalExaminations());
    }

    @PostMapping
    public ResponseEntity<MedicalExaminationResponseDTO> create(
            @Valid @RequestBody MedicalExaminationRequestDTO requestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(medicalExaminationService.createMedicalExamination(requestDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MedicalExaminationResponseDTO> update(
            @PathVariable String id,
            @Valid @RequestBody MedicalExaminationUpdateDTO updateDTO) {
        return ResponseEntity.ok(medicalExaminationService.updateMedicalExamination(id, updateDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        medicalExaminationService.deleteMedicalExamination(id);
        return ResponseEntity.noContent().build();
    }
}