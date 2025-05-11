package com.oncologic.clinic.controller.rest.patient;

import com.oncologic.clinic.dto.patient.request.ObservationRequestDTO;
import com.oncologic.clinic.dto.patient.response.ObservationResponseDTO;
import com.oncologic.clinic.dto.patient.update.ObservationUpdateDTO;
import com.oncologic.clinic.service.patient.ObservationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/observations")
public class ObservationRestController {
    private final ObservationService observationService;

    public ObservationRestController(ObservationService observationService) {
        this.observationService = observationService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ObservationResponseDTO> getById(@PathVariable Long id) {
        ObservationResponseDTO observation = observationService.getObservationById(id);
        return ResponseEntity.ok(observation); // 200 OK
    }

    @GetMapping
    public ResponseEntity<List<ObservationResponseDTO>> getAll() {
        List<ObservationResponseDTO> observations = observationService.getAllObservations();
        return ResponseEntity.ok(observations); // 200 OK
    }

    @PostMapping
    public ResponseEntity<ObservationResponseDTO> create(@Valid @RequestBody ObservationRequestDTO dto) {
        ObservationResponseDTO created = observationService.createObservation(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created); // 201 Created
    }

    @PutMapping("/{id}")
    public ResponseEntity<ObservationResponseDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody ObservationUpdateDTO dto) {
        ObservationResponseDTO updated = observationService.updateObservation(id, dto);
        return ResponseEntity.ok(updated); // 200 OK
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        observationService.deleteObservation(id);
        return ResponseEntity.noContent().build(); // 204 No Content
    }
}
