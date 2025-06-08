package com.oncologic.clinic.controller.rest.personal;

import com.oncologic.clinic.dto.personal.DoctorDTO;
import com.oncologic.clinic.dto.personal.response.DoctorResponseDTO;
import com.oncologic.clinic.service.personal.DoctorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Doctors", description = "Operations related to doctor management")
@RestController
@RequestMapping("/api/doctors")
@RequiredArgsConstructor
public class DoctorRestController {

    private final DoctorService doctorService;

    @Operation(summary = "Create a new doctor")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Doctor created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PostMapping
    public ResponseEntity<DoctorResponseDTO> createDoctor(
            @Valid @RequestBody DoctorDTO doctorDTO) {
        DoctorResponseDTO createdDoctor = doctorService.createDoctor(doctorDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdDoctor);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DoctorResponseDTO> getDoctorById(@PathVariable Long id) {
        return ResponseEntity.ok(doctorService.getDoctorById(id));
    }

    @Operation(summary = "Get all doctors")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of doctors retrieved")
    })
    @GetMapping
    public ResponseEntity<List<DoctorResponseDTO>> getAllDoctors() {
        return ResponseEntity.ok(doctorService.getAllDoctors());
    }

    @Operation(summary = "Update doctor information")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Doctor updated successfully"),
            @ApiResponse(responseCode = "404", description = "Doctor not found"),
            @ApiResponse(responseCode = "400", description = "Invalid update data")
    })
    @PutMapping("/{id}")
    public ResponseEntity<DoctorResponseDTO> updateDoctor(
            @PathVariable Long id,
            @Valid @RequestBody DoctorDTO doctorDTO) {
        return ResponseEntity.ok(doctorService.updateDoctor(id, doctorDTO));
    }

    @Operation(summary = "Delete doctor")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Doctor deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Doctor not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDoctor(@PathVariable Long id) {
        doctorService.deleteDoctor(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Search doctors by name")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Doctors found")
    })
    @GetMapping("/search")
    public ResponseEntity<List<DoctorResponseDTO>> searchDoctorsByName(
            @RequestParam String name) {
        return ResponseEntity.ok(doctorService.searchDoctorsByName(name));
    }

    @Operation(summary = "Get doctors by speciality")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Doctors found"),
            @ApiResponse(responseCode = "404", description = "Speciality not found")
    })
    @GetMapping("/by-speciality/{specialityId}")
    public ResponseEntity<List<DoctorResponseDTO>> getDoctorsBySpeciality(
            @PathVariable Long specialityId) {
        return ResponseEntity.ok(doctorService.getDoctorsBySpeciality(specialityId));
    }

}