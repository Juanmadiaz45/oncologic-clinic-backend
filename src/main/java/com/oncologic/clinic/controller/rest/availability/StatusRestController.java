package com.oncologic.clinic.controller.rest.availability;

import com.oncologic.clinic.dto.availability.StatusDTO;
import com.oncologic.clinic.dto.availability.response.StatusResponseDTO;
import com.oncologic.clinic.service.availability.StatusService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/statuses")
@RequiredArgsConstructor
public class StatusRestController {

    private final StatusService statusService;

    @PostMapping
    public ResponseEntity<StatusResponseDTO> createStatus(
            @Valid @RequestBody StatusDTO requestDTO) {
        StatusResponseDTO createdStatus = statusService.createStatus(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdStatus);
    }

    @GetMapping("/{id}")
    public ResponseEntity<StatusResponseDTO> getStatusById(@PathVariable Long id) {
        return ResponseEntity.ok(statusService.getStatusById(id));
    }

    @GetMapping
    public ResponseEntity<List<StatusResponseDTO>> getAllStatuses() {
        return ResponseEntity.ok(statusService.getAllStatuses());
    }

    @PutMapping("/{id}")
    public ResponseEntity<StatusResponseDTO> updateStatus(
            @PathVariable Long id,
            @Valid @RequestBody StatusDTO updateDTO) {
        return ResponseEntity.ok(statusService.updateStatus(id, updateDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStatus(@PathVariable Long id) {
        statusService.deleteStatus(id);
        return ResponseEntity.noContent().build();
    }
}