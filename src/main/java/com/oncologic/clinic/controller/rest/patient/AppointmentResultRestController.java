package com.oncologic.clinic.controller.rest.patient;

import com.oncologic.clinic.dto.patient.request.AppointmentResultRequestDTO;
import com.oncologic.clinic.dto.patient.response.AppointmentResultResponseDTO;
import com.oncologic.clinic.service.patient.AppointmentResultService;
import jakarta.validation.Valid;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/appointment-results")
public class AppointmentResultRestController {
    private final AppointmentResultService appointmentResultService;

    public AppointmentResultRestController(AppointmentResultService appointmentResultService) {
        this.appointmentResultService = appointmentResultService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<AppointmentResultResponseDTO> getById(@PathVariable Long id) {
        AppointmentResultResponseDTO result = appointmentResultService.getAppointmentResultById(id);
        return ResponseEntity.ok(result); // 200 OK
    }

    @GetMapping
    public ResponseEntity<List<AppointmentResultResponseDTO>> getAll() {
        List<AppointmentResultResponseDTO> results = appointmentResultService.getAllAppointmentResults();
        return ResponseEntity.ok(results); // 200 OK
    }

    @PostMapping
    public ResponseEntity<AppointmentResultResponseDTO> create(@Valid @RequestBody AppointmentResultRequestDTO dto) {
        AppointmentResultResponseDTO created = appointmentResultService.createAppointmentResult(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created); // 201 Created
    }

    @PutMapping("/{id}")
    public ResponseEntity<AppointmentResultResponseDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody AppointmentResultRequestDTO dto) {
        AppointmentResultResponseDTO updated = appointmentResultService.updateAppointmentResult(id, dto);
        return ResponseEntity.ok(updated); // 200 OK
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        try {
            appointmentResultService.deleteAppointmentResult(id);
            return ResponseEntity.noContent().build();
        } catch (DataIntegrityViolationException ex) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Cannot be deleted because it has associated records", ex);
        }
    }
}
