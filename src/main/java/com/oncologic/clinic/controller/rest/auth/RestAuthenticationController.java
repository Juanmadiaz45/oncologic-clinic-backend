package com.oncologic.clinic.controller.rest.auth;


import com.oncologic.clinic.dto.auth.AuthRequestDTO;
import com.oncologic.clinic.dto.auth.AuthResponseDTO;
import com.oncologic.clinic.service.auth.interfaces.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "Authentication", description = "Endpoints for user authentication")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class RestAuthenticationController {

    private final AuthenticationService authenticationService;

    @Operation(summary = "Authenticate a user and return a token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login successful"),
            @ApiResponse(responseCode = "401", description = "Invalid credentials"),
            @ApiResponse(responseCode = "400", description = "Missing or malformed data")
    })
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody AuthRequestDTO authRequestDTO) {
        AuthResponseDTO authResponse = authenticationService.login(authRequestDTO);
        return ResponseEntity.ok(authResponse);
    }

}