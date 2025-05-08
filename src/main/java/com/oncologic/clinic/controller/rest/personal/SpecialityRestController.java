package com.oncologic.clinic.controller.rest.personal;

import com.oncologic.clinic.dto.personal.request.SpecialityRequestDTO;
import com.oncologic.clinic.dto.personal.response.SpecialityResponseDTO;
import com.oncologic.clinic.dto.personal.update.SpecialityUpdateDTO;
import com.oncologic.clinic.service.personal.SpecialityService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/specialities")
@RequiredArgsConstructor
public class SpecialityRestController {

    private final SpecialityService specialityService;

    @PostMapping
    public ResponseEntity<SpecialityResponseDTO> createSpeciality(
            @Valid @RequestBody SpecialityRequestDTO specialityDTO) {
        SpecialityResponseDTO createdSpeciality = specialityService.createSpeciality(specialityDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdSpeciality);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SpecialityResponseDTO> getSpecialityById(@PathVariable Long id) {
        return ResponseEntity.ok(specialityService.getSpecialityById(id));
    }

    @GetMapping
    public ResponseEntity<List<SpecialityResponseDTO>> getAllSpecialities() {
        return ResponseEntity.ok(specialityService.getAllSpecialities());
    }

    @PutMapping("/{id}")
    public ResponseEntity<SpecialityResponseDTO> updateSpeciality(
            @PathVariable Long id,
            @Valid @RequestBody SpecialityUpdateDTO specialityDTO) {
        return ResponseEntity.ok(specialityService.updateSpeciality(id, specialityDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSpeciality(@PathVariable Long id) {
        specialityService.deleteSpeciality(id);
        return ResponseEntity.noContent().build();
    }
}