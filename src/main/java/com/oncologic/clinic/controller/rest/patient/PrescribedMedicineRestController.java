package com.oncologic.clinic.controller.rest.patient;

import com.oncologic.clinic.dto.patient.request.PrescribedMedicineRequestDTO;
import com.oncologic.clinic.dto.patient.response.PrescribedMedicineResponseDTO;
import com.oncologic.clinic.dto.patient.update.PrescribedMedicineUpdateDTO;
import com.oncologic.clinic.service.patient.PrescribedMedicineService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/prescribed-medicines")
public class PrescribedMedicineRestController {
    private final PrescribedMedicineService prescribedMedicineService;

    public PrescribedMedicineRestController(PrescribedMedicineService prescribedMedicineService) {
        this.prescribedMedicineService = prescribedMedicineService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<PrescribedMedicineResponseDTO> getById(@PathVariable Long id) {
        PrescribedMedicineResponseDTO response = prescribedMedicineService.getPrescribedMedicineById(id);
        return ResponseEntity.ok(response); // 200 OK
    }

    @GetMapping
    public ResponseEntity<List<PrescribedMedicineResponseDTO>> getAll() {
        List<PrescribedMedicineResponseDTO> response = prescribedMedicineService.getAllPrescribedMedicines();
        return ResponseEntity.ok(response); // 200 OK
    }

    @PostMapping
    public ResponseEntity<PrescribedMedicineResponseDTO> create(@Valid @RequestBody PrescribedMedicineRequestDTO dto) {
        PrescribedMedicineResponseDTO created = prescribedMedicineService.createPrescribedMedicine(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created); // 201 Created
    }

    @PutMapping("/{id}")
    public ResponseEntity<PrescribedMedicineResponseDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody PrescribedMedicineUpdateDTO dto) {
        PrescribedMedicineResponseDTO updated = prescribedMedicineService.updatePrescribedMedicine(id, dto);
        return ResponseEntity.ok(updated); // 200 OK
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        prescribedMedicineService.deletePrescribedMedicine(id);
        return ResponseEntity.noContent().build(); // 204 No Content
    }
}
