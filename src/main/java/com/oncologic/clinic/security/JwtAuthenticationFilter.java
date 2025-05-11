package com.oncologic.clinic.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final HandlerExceptionResolver handlerExceptionResolver;
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    /**
     * Method that intercepts each HTTP request and checks if there's a JWT token present in the header.
     * If the token is valid, it authenticates the user
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        // Get the 'Authorization' header from the request
        final String authHeader = request.getHeader("Authorization");

        // If the header is not present or doesn't contain a Bearer token, continue with the next filter
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            // Extract the JWT token from the header
            final String jwt = authHeader.substring(7);
            // Get the username from the token
            final String username = jwtService.extractUsername(jwt);

            // Check if the user is not authenticated in the security context
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (username != null && authentication == null) {
                // Load user details from the database
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                // If the token is valid, create the authentication object
                if (jwtService.isTokenValid(jwt, userDetails)) {
                    // Create an authentication token based on the user and their roles
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());

                    // Set the request details in the authentication token
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    // Place the authentication token in Spring's security context
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                } else {
                    SecurityContextHolder.clearContext();  // ← Clears the context if the token is invalid
                }
            }

            filterChain.doFilter(request, response);
        } catch (Exception exception) {
            handlerExceptionResolver.resolveException(request, response, null, exception);
        }
    }

}