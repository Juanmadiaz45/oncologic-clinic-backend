package com.oncologic.clinic.controller.rest.patient;

import com.oncologic.clinic.dto.patient.request.TypeOfTreatmentRequestDTO;
import com.oncologic.clinic.dto.patient.response.TypeOfTreatmentResponseDTO;
import com.oncologic.clinic.dto.patient.update.TypeOfTreatmentUpdateDTO;
import com.oncologic.clinic.service.patient.TypeOfTreatmentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/type-of-treatments")
public class TypeOfTreatmentRestController {
    private final TypeOfTreatmentService typeOfTreatmentService;

    public TypeOfTreatmentRestController(TypeOfTreatmentService typeOfTreatmentService) {
        this.typeOfTreatmentService = typeOfTreatmentService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<TypeOfTreatmentResponseDTO> getById(@PathVariable Long id) {
        TypeOfTreatmentResponseDTO response = typeOfTreatmentService.getTypeOfTreatmentById(id);
        return ResponseEntity.ok(response); // 200 OK
    }

    @GetMapping
    public ResponseEntity<List<TypeOfTreatmentResponseDTO>> getAll() {
        List<TypeOfTreatmentResponseDTO> response = typeOfTreatmentService.getAllTypesOfTreatment();
        return ResponseEntity.ok(response); // 200 OK
    }

    @PostMapping
    public ResponseEntity<TypeOfTreatmentResponseDTO> create(@Valid @RequestBody TypeOfTreatmentRequestDTO dto) {
        TypeOfTreatmentResponseDTO created = typeOfTreatmentService.createTypeOfTreatment(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created); // 201 Created
    }

    @PutMapping("/{id}")
    public ResponseEntity<TypeOfTreatmentResponseDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody TypeOfTreatmentUpdateDTO dto) {
        TypeOfTreatmentResponseDTO updated = typeOfTreatmentService.updateTypeOfTreatment(id, dto);
        return ResponseEntity.ok(updated); // 200 OK
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        typeOfTreatmentService.deleteTypeOfTreatment(id);
        return ResponseEntity.noContent().build(); // 204 No Content
    }
}
