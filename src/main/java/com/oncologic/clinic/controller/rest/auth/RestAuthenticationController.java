package com.oncologic.clinic.controller.rest.auth;


import com.oncologic.clinic.controller.rest.BaseApiController;
import com.oncologic.clinic.dto.auth.AuthRequestDTO;
import com.oncologic.clinic.dto.auth.AuthResponseDTO;
import com.oncologic.clinic.service.auth.interfaces.AuthenticationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class RestAuthenticationController extends BaseApiController {

    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody AuthRequestDTO authRequestDTO) {
        AuthResponseDTO authResponse = authenticationService.login(authRequestDTO);
        return ResponseEntity.ok(authResponse);
    }

}