package com.oncologic.clinic.service.auth.impl;

import com.oncologic.clinic.dto.auth.AuthRequestDTO;
import com.oncologic.clinic.dto.auth.AuthResponseDTO;
import com.oncologic.clinic.entity.user.User;
import com.oncologic.clinic.security.JwtService;
import com.oncologic.clinic.service.auth.interfaces.AuthenticationService;
import com.oncologic.clinic.service.user.UserService;
import com.oncologic.clinic.service.user.impl.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final CustomUserDetailsService customUserDetailService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserService userService; // Inyección del UserService

    @Override
    public AuthResponseDTO login(AuthRequestDTO request) {
        try {
            // Authenticate user
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Invalid username or password");
        }

        // Find an authenticated user
        UserDetails userDetails = customUserDetailService.loadUserByUsername(request.getUsername());

        // Get user entity and convert to UserResponseDTO
        User user = userService.getUserByUsername(request.getUsername());

        // Generate JWT token
        String jwtToken = jwtService.generateToken(userDetails);

        return AuthResponseDTO.builder()
                .accessToken(jwtToken)
                .user(userService.getUserById(user.getId())) // Obtener UserResponseDTO
                .build();
    }
}