package com.oncologic.clinic.controller.rest.appointment;

import com.oncologic.clinic.dto.appointment.MedicalOfficeDTO;
import com.oncologic.clinic.dto.appointment.response.MedicalOfficeResponseDTO;
import com.oncologic.clinic.service.appointment.MedicalOfficeService;
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
import java.util.Map;

@Tag(name = "Medical Offices", description = "Operations related to medical office management")
@RestController
@RequestMapping("/api/medical-offices")
@RequiredArgsConstructor
public class MedicalOfficeRestController {

    private final MedicalOfficeService service;

    @Operation(summary = "Get a medical office by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Medical office found"),
            @ApiResponse(responseCode = "404", description = "Medical office not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<MedicalOfficeResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getMedicalOfficeById(id));
    }

    @Operation(summary = "Get all medical offices")
    @ApiResponse(responseCode = "200", description = "List of all medical offices")
    @GetMapping
    public ResponseEntity<List<MedicalOfficeResponseDTO>> getAll() {
        return ResponseEntity.ok(service.getAllMedicalOffices());
    }

    @Operation(summary = "Create a new medical office")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Medical office created"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PostMapping
    public ResponseEntity<MedicalOfficeResponseDTO> create(
            @Valid @RequestBody MedicalOfficeDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.createMedicalOffice(dto));
    }

    @Operation(summary = "Update an existing medical office")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Medical office updated"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Medical office not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<MedicalOfficeResponseDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody MedicalOfficeDTO dto) {
        return ResponseEntity.ok(service.updateMedicalOffice(id, dto));
    }

    @Operation(summary = "Delete a medical office")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Medical office deleted"),
            @ApiResponse(responseCode = "404", description = "Medical office not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteMedicalOffice(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get available medical offices for a time slot")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Available offices found"),
            @ApiResponse(responseCode = "400", description = "Invalid time parameters")
    })
    @PostMapping("/available")
    public ResponseEntity<List<MedicalOfficeResponseDTO>> getAvailableOffices(
            @RequestBody Map<String, String> params) {
        String date = params.get("date");
        String startTime = params.get("startTime");
        String endTime = params.get("endTime");

        return ResponseEntity.ok(service.getAvailableOffices(date, startTime, endTime));
    }

}
