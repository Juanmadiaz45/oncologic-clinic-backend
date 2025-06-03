package com.oncologic.clinic.controller.rest.personal;

import com.oncologic.clinic.dto.personal.SpecialityDTO;
import com.oncologic.clinic.dto.personal.response.SpecialityResponseDTO;
import com.oncologic.clinic.service.personal.SpecialityService;
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

@Tag(name = "Specialities", description = "Operations related to medical specialities")
@RestController
@RequestMapping("/api/specialities")
@RequiredArgsConstructor
public class SpecialityRestController {

    private final SpecialityService specialityService;

    @Operation(summary = "Create a new speciality")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Speciality created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PostMapping
    public ResponseEntity<SpecialityResponseDTO> createSpeciality(
            @Valid @RequestBody SpecialityDTO specialityDTO) {
        SpecialityResponseDTO createdSpeciality = specialityService.createSpeciality(specialityDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdSpeciality);
    }

    @Operation(summary = "Get a speciality by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Speciality found"),
            @ApiResponse(responseCode = "404", description = "Speciality not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<SpecialityResponseDTO> getSpecialityById(@PathVariable Long id) {
        return ResponseEntity.ok(specialityService.getSpecialityById(id));
    }

    @Operation(summary = "Get all specialities")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of specialities retrieved")
    })
    @GetMapping
    public ResponseEntity<List<SpecialityResponseDTO>> getAllSpecialities() {
        return ResponseEntity.ok(specialityService.getAllSpecialities());
    }

    @Operation(summary = "Update a speciality")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Speciality updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid update data"),
            @ApiResponse(responseCode = "404", description = "Speciality not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<SpecialityResponseDTO> updateSpeciality(
            @PathVariable Long id,
            @Valid @RequestBody SpecialityDTO specialityDTO) {
        return ResponseEntity.ok(specialityService.updateSpeciality(id, specialityDTO));
    }

    @Operation(summary = "Delete a speciality")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Speciality deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Speciality not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSpeciality(@PathVariable Long id) {
        specialityService.deleteSpeciality(id);
        return ResponseEntity.noContent().build();
    }
}