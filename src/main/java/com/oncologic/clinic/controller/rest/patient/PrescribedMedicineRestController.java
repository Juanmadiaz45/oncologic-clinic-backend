package com.oncologic.clinic.controller.rest.patient;

import com.oncologic.clinic.dto.patient.request.PrescribedMedicineRequestDTO;
import com.oncologic.clinic.dto.patient.response.PrescribedMedicineResponseDTO;
import com.oncologic.clinic.dto.patient.update.PrescribedMedicineUpdateDTO;
import com.oncologic.clinic.service.patient.PrescribedMedicineService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Prescribed Medicines", description = "Operations related to prescribed medicines")
@RestController
@RequestMapping("/api/prescribed-medicines")
public class PrescribedMedicineRestController {
    private final PrescribedMedicineService prescribedMedicineService;

    public PrescribedMedicineRestController(PrescribedMedicineService prescribedMedicineService) {
        this.prescribedMedicineService = prescribedMedicineService;
    }

    @Operation(summary = "Get prescribed medicine by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Prescribed medicine found"),
            @ApiResponse(responseCode = "404", description = "Prescribed medicine not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<PrescribedMedicineResponseDTO> getById(@PathVariable Long id) {
        PrescribedMedicineResponseDTO response = prescribedMedicineService.getPrescribedMedicineById(id);
        return ResponseEntity.ok(response); // 200 OK
    }

    @Operation(summary = "Get all prescribed medicines")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of prescribed medicines")
    })
    @GetMapping
    public ResponseEntity<List<PrescribedMedicineResponseDTO>> getAll() {
        List<PrescribedMedicineResponseDTO> response = prescribedMedicineService.getAllPrescribedMedicines();
        return ResponseEntity.ok(response); // 200 OK
    }

    @Operation(summary = "Create a new prescribed medicine")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Prescribed medicine created"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PostMapping
    public ResponseEntity<PrescribedMedicineResponseDTO> create(@Valid @RequestBody PrescribedMedicineRequestDTO dto) {
        PrescribedMedicineResponseDTO created = prescribedMedicineService.createPrescribedMedicine(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created); // 201 Created
    }

    @Operation(summary = "Update an existing prescribed medicine")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Prescribed medicine updated"),
            @ApiResponse(responseCode = "404", description = "Prescribed medicine not found"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PutMapping("/{id}")
    public ResponseEntity<PrescribedMedicineResponseDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody PrescribedMedicineUpdateDTO dto) {
        PrescribedMedicineResponseDTO updated = prescribedMedicineService.updatePrescribedMedicine(id, dto);
        return ResponseEntity.ok(updated); // 200 OK
    }

    @Operation(summary = "Delete a prescribed medicine")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Prescribed medicine deleted"),
            @ApiResponse(responseCode = "404", description = "Prescribed medicine not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        prescribedMedicineService.deletePrescribedMedicine(id);
        return ResponseEntity.noContent().build(); // 204 No Content
    }
}
