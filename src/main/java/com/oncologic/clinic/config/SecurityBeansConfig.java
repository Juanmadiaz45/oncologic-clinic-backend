package com.oncologic.clinic.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SecurityBeansConfig {

    /**
     * Defines a bean for encoding passwords using the BCrypt hashing algorithm.
     * Used to enhance application security by securely storing user passwords.
     *
     * @return a {@link PasswordEncoder} instance that uses BCrypt for password encoding
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Defines a bean for configuring the authentication provider used in the application.
     * This provider is responsible for authenticating users by verifying their credentials
     * against the provided user details service and password encoder.
     *
     * @param userDetailsService the service for loading user-specific data
     * @param passwordEncoder    the encoder used for hashing and validating passwords
     * @return an {@link AuthenticationProvider} instance configured with the specified user details service and password encoder
     */
    @Bean
    public AuthenticationProvider authenticationProvider(
            UserDetailsService userDetailsService,
            PasswordEncoder passwordEncoder
    ) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }
}