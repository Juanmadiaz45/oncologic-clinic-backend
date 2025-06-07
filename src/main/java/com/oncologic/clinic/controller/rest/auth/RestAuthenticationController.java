package com.oncologic.clinic.controller.rest.auth;


import com.oncologic.clinic.dto.auth.AuthRequestDTO;
import com.oncologic.clinic.dto.auth.AuthResponseDTO;
import com.oncologic.clinic.security.JwtService;
import com.oncologic.clinic.service.auth.interfaces.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@Tag(name = "Authentication", description = "Endpoints for user authentication")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class RestAuthenticationController {

    private final AuthenticationService authenticationService;
    private final JwtService jwtService;

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

    @PostMapping("/validate-token")
    public ResponseEntity<Map<String, Object>> validateToken(HttpServletRequest request) {
        try {
            // Extract token from Authorization header
            String authHeader = request.getHeader("Authorization");

            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.badRequest()
                        .body(Map.of(
                                "valid", false,
                                "reason", "no_token",
                                "message", "No token provided"
                        ));
            }

            String token = authHeader.substring(7); // Remove "Bearer " prefix

            // Validate token using JWT service
            if (jwtService.isTokenExpired(token)) {
                return ResponseEntity.status(401)
                        .body(Map.of(
                                "valid", false,
                                "reason", "expired",
                                "message", "Token has expired"
                        ));
            }

            // Token is valid
            return ResponseEntity.ok(Map.of(
                    "valid", true,
                    "message", "Token is valid"
            ));

        } catch (Exception e) {
            return ResponseEntity.status(401)
                    .body(Map.of(
                            "valid", false,
                            "reason", "error",
                            "message", "Error validating token: " + e.getMessage()
                    ));
        }
    }

}