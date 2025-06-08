package com.oncologic.clinic.controller.rest.personal;

import com.oncologic.clinic.dto.availability.response.AvailabilityResponseDTO;
import com.oncologic.clinic.service.personal.PersonalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Personal", description = "Operations related to personnel management")
@RestController
@RequestMapping("/api/personal")
@RequiredArgsConstructor
public class PersonalRestController {

    private final PersonalService personalService;

    @Operation(summary = "Get personal availabilities by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Availabilities found"),
            @ApiResponse(responseCode = "404", description = "Personal not found")
    })
    @GetMapping("/{id}/availabilities")
    public ResponseEntity<List<AvailabilityResponseDTO>> getPersonalAvailabilities(
            @PathVariable Long id) {
        return ResponseEntity.ok(personalService.getPersonalAvailabilities(id));
    }
}