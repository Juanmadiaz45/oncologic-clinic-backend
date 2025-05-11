package com.oncologic.clinic.controller.rest.appointment;

import com.oncologic.clinic.dto.appointment.MedicalTaskDTO;
import com.oncologic.clinic.dto.appointment.response.MedicalTaskResponseDTO;
import com.oncologic.clinic.service.appointment.MedicalTaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/medical-tasks")
@RequiredArgsConstructor
public class MedicalTaskRestController {

    private final MedicalTaskService service;

    @GetMapping("/{id}")
    public ResponseEntity<MedicalTaskResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getMedicalTaskById(id));
    }

    @GetMapping
    public ResponseEntity<List<MedicalTaskResponseDTO>> getAll() {
        return ResponseEntity.ok(service.getAllMedicalTasks());
    }

    @PostMapping
    public ResponseEntity<MedicalTaskResponseDTO> create(
            @Valid @RequestBody MedicalTaskDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.createMedicalTask(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MedicalTaskResponseDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody MedicalTaskDTO dto) {
        return ResponseEntity.ok(service.updateMedicalTask(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteMedicalTask(id);
        return ResponseEntity.noContent().build();
    }
}
