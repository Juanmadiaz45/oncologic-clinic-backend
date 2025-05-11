package com.oncologic.clinic.controller.rest.appointment;

import com.oncologic.clinic.dto.appointment.MedicalOfficeDTO;
import com.oncologic.clinic.dto.appointment.response.MedicalOfficeResponseDTO;
import com.oncologic.clinic.service.appointment.MedicalOfficeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/medical-offices")
@RequiredArgsConstructor
public class MedicalOfficeRestController {

    private final MedicalOfficeService service;

    @GetMapping("/{id}")
    public ResponseEntity<MedicalOfficeResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getMedicalOfficeById(id));
    }

    @GetMapping
    public ResponseEntity<List<MedicalOfficeResponseDTO>> getAll() {
        return ResponseEntity.ok(service.getAllMedicalOffices());
    }

    @PostMapping
    public ResponseEntity<MedicalOfficeResponseDTO> create(
            @Valid @RequestBody MedicalOfficeDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.createMedicalOffice(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MedicalOfficeResponseDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody MedicalOfficeDTO dto) {
        return ResponseEntity.ok(service.updateMedicalOffice(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteMedicalOffice(id);
        return ResponseEntity.noContent().build();
    }
}
