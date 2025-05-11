package com.oncologic.clinic.controller.rest.appointment;

import com.oncologic.clinic.dto.appointment.TypeOfMedicalAppointmentDTO;
import com.oncologic.clinic.dto.appointment.response.TypeOfMedicalAppointmentResponseDTO;
import com.oncologic.clinic.service.appointment.TypeOfMedicalAppointmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/medical-appointment-types")
@RequiredArgsConstructor
public class TypeOfMedicalAppointmentRestController {

    private final TypeOfMedicalAppointmentService service;

    @GetMapping("/{id}")
    public ResponseEntity<TypeOfMedicalAppointmentResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getTypeOfMedicalAppointmentById(id));
    }

    @GetMapping
    public ResponseEntity<List<TypeOfMedicalAppointmentResponseDTO>> getAll() {
        return ResponseEntity.ok(service.getAllTypesOfMedicalAppointment());
    }

    @PostMapping
    public ResponseEntity<TypeOfMedicalAppointmentResponseDTO> create(
            @Valid @RequestBody TypeOfMedicalAppointmentDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.createTypeOfMedicalAppointment(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TypeOfMedicalAppointmentResponseDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody TypeOfMedicalAppointmentDTO dto) {
        return ResponseEntity.ok(service.updateTypeOfMedicalAppointment(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteTypeOfMedicalAppointment(id);
        return ResponseEntity.noContent().build();
    }
}
