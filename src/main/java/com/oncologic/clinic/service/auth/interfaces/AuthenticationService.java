package com.oncologic.clinic.service.auth.interfaces;


import com.oncologic.clinic.dto.auth.AuthRequestDTO;
import com.oncologic.clinic.dto.auth.AuthResponseDTO;

public interface AuthenticationService {

    AuthResponseDTO login(AuthRequestDTO request);

    // AuthResponseDTO register(RegisterDTO request);

}