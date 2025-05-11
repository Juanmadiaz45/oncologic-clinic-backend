package com.oncologic.clinic.controller.rest.availability;

import com.oncologic.clinic.dto.availability.AvailabilityDTO;
import com.oncologic.clinic.dto.availability.response.AvailabilityResponseDTO;
import com.oncologic.clinic.service.availability.AvailabilityService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/availabilities")
@RequiredArgsConstructor
public class AvailabilityRestController {

    private final AvailabilityService availabilityService;

    @PostMapping
    public ResponseEntity<AvailabilityResponseDTO> createAvailability(
            @Valid @RequestBody AvailabilityDTO requestDTO) {
        AvailabilityResponseDTO createdAvailability = availabilityService.createAvailability(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdAvailability);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AvailabilityResponseDTO> getAvailabilityById(@PathVariable Long id) {
        return ResponseEntity.ok(availabilityService.getAvailabilityById(id));
    }

    @GetMapping
    public ResponseEntity<List<AvailabilityResponseDTO>> getAllAvailabilities() {
        return ResponseEntity.ok(availabilityService.getAllAvailabilities());
    }

    @PutMapping("/{id}")
    public ResponseEntity<AvailabilityResponseDTO> updateAvailability(
            @PathVariable Long id,
            @Valid @RequestBody AvailabilityDTO updateDTO) {
        return ResponseEntity.ok(availabilityService.updateAvailability(id, updateDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAvailability(@PathVariable Long id) {
        availabilityService.deleteAvailability(id);
        return ResponseEntity.noContent().build();
    }
}