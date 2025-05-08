package com.oncologic.clinic.controller.rest.personal;

import com.oncologic.clinic.dto.personal.request.DoctorRequestDTO;
import com.oncologic.clinic.dto.personal.response.DoctorResponseDTO;
import com.oncologic.clinic.dto.personal.update.DoctorUpdateDTO;
import com.oncologic.clinic.dto.registration.RegisterDoctorDTO;
import com.oncologic.clinic.service.personal.DoctorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/doctors")
@RequiredArgsConstructor
public class DoctorRestController {

    private final DoctorService doctorService;

    @PostMapping
    public ResponseEntity<DoctorResponseDTO> createDoctor(
            @Valid @RequestBody DoctorRequestDTO doctorDTO) {
        DoctorResponseDTO createdDoctor = doctorService.createDoctor(doctorDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdDoctor);
    }

    @PostMapping("/register")
    public ResponseEntity<DoctorResponseDTO> registerDoctor(
            @Valid @RequestBody RegisterDoctorDTO registerDoctorDTO) {
        DoctorResponseDTO registeredDoctor = doctorService.createDoctor(mapRegisterToDoctorDTO(registerDoctorDTO));
        return ResponseEntity.status(HttpStatus.CREATED).body(registeredDoctor);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DoctorResponseDTO> getDoctorById(@PathVariable Long id) {
        return ResponseEntity.ok(doctorService.getDoctorById(id));
    }

    @GetMapping
    public ResponseEntity<List<DoctorResponseDTO>> getAllDoctors() {
        return ResponseEntity.ok(doctorService.getAllDoctors());
    }

    @PutMapping("/{id}")
    public ResponseEntity<DoctorResponseDTO> updateDoctor(
            @PathVariable Long id,
            @Valid @RequestBody DoctorUpdateDTO doctorDTO) {
        return ResponseEntity.ok(doctorService.updateDoctor(id, doctorDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDoctor(@PathVariable Long id) {
        doctorService.deleteDoctor(id);
        return ResponseEntity.noContent().build();
    }

    private DoctorRequestDTO mapRegisterToDoctorDTO(RegisterDoctorDTO registerDTO) {
        // Implementación del mapeo según la estructura de tus DTOs
        DoctorRequestDTO doctorDTO = new DoctorRequestDTO();
        // Mapear campos necesarios
        return doctorDTO;
    }
}