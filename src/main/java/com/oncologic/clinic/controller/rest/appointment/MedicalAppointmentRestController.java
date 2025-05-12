package com.oncologic.clinic.controller.rest.appointment;

import com.oncologic.clinic.dto.appointment.MedicalAppointmentDTO;
import com.oncologic.clinic.dto.appointment.response.MedicalAppointmentResponseDTO;
import com.oncologic.clinic.service.appointment.MedicalAppointmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/medical-appointments")
@RequiredArgsConstructor
public class MedicalAppointmentRestController {

    private final MedicalAppointmentService service;

    @GetMapping("/{id}")
    public ResponseEntity<MedicalAppointmentResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getMedicalAppointmentById(id));
    }

    @GetMapping
    public ResponseEntity<List<MedicalAppointmentResponseDTO>> getAll() {
        return ResponseEntity.ok(service.getAllMedicalAppointments());
    }

    @PostMapping
    public ResponseEntity<MedicalAppointmentResponseDTO> create(@Valid @RequestBody MedicalAppointmentDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createMedicalAppointment(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MedicalAppointmentResponseDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody MedicalAppointmentDTO dto) {
        return ResponseEntity.ok(service.updateMedicalAppointment(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteMedicalAppointment(id);
        return ResponseEntity.noContent().build();
    }
}
