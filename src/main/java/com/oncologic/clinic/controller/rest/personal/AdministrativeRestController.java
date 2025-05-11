package com.oncologic.clinic.controller.rest.personal;

import com.oncologic.clinic.dto.personal.AdministrativeDTO;
import com.oncologic.clinic.dto.personal.response.AdministrativeResponseDTO;
import com.oncologic.clinic.service.personal.AdministrativeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/administratives")
@RequiredArgsConstructor
public class AdministrativeRestController {

    private final AdministrativeService administrativeService;

    @PostMapping
    public ResponseEntity<AdministrativeResponseDTO> createAdministrative(
            @Valid @RequestBody AdministrativeDTO administrativeDTO) {
        AdministrativeResponseDTO createdAdministrative = administrativeService.createAdministrative(administrativeDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdAdministrative);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AdministrativeResponseDTO> getAdministrativeById(@PathVariable Long id) {
        return ResponseEntity.ok(administrativeService.getAdministrativeById(id));
    }

    @GetMapping
    public ResponseEntity<List<AdministrativeResponseDTO>> getAllAdministratives() {
        return ResponseEntity.ok(administrativeService.getAllAdministrative());
    }

    @PutMapping("/{id}")
    public ResponseEntity<AdministrativeResponseDTO> updateAdministrative(
            @PathVariable Long id,
            @Valid @RequestBody AdministrativeDTO administrativeDTO) {
        return ResponseEntity.ok(administrativeService.updateAdministrative(id, administrativeDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAdministrative(@PathVariable Long id) {
        administrativeService.deleteAdministrative(id);
        return ResponseEntity.noContent().build();
    }
}