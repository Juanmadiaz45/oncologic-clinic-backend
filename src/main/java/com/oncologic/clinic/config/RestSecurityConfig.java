package com.oncologic.clinic.config;

import com.oncologic.clinic.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Collections;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class RestSecurityConfig {

    private final AuthenticationProvider authenticationProvider;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    /**
     * Defines the security filter chain that will be applied to all HTTP requests.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                // Disable CSRF protection as it will be handled via JWT tokens
                .csrf(AbstractHttpConfigurer::disable)
                // Disable CORS as it will be manually configured with a separate filter
                .cors(AbstractHttpConfigurer::disable)
                // Configuration of HTTP request authorization rules
                .authorizeHttpRequests(authRequest -> authRequest
                        // Allow access to static resources
                        .requestMatchers("/styles/**", "/images/**").permitAll()
                        // Allow all requests starting with /auth, such as login or registration
                        .requestMatchers("/api/auth/**").permitAll()
                        // Allow SSE connections
                        // Any other request must be authenticated
                        .anyRequest().authenticated()
                )
                // Set the session management policy to STATELESS (no server-side session storage)
                .sessionManagement(sessionManager -> sessionManager
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // Configure the authentication provider that validates credentials
                .authenticationProvider(authenticationProvider)
                // Add a JWT authentication filter before the username/password authentication filter
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    /**
     * Configures a global CORS filter to handle requests from any origin.
     */
    @Bean
    public FilterRegistrationBean<?> simpleCorsFilter() {
        // URL-based CORS configuration source
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();

        // Allow sending cookies and credentials
        config.setAllowCredentials(true);

        // Allow all domains
        config.setAllowedOriginPatterns(Collections.singletonList("*"));

        // Allow all HTTP methods
        config.setAllowedMethods(Collections.singletonList("*"));

        // Allow all headers
        config.setAllowedHeaders(Collections.singletonList("*"));

        // Register configuration for all routes (/**)
        source.registerCorsConfiguration("/**", config);

        // Register filter with defined configuration and set the highest priority
        FilterRegistrationBean<?> bean = new FilterRegistrationBean<>(new CorsFilter(source));
        bean.setOrder(Ordered.HIGHEST_PRECEDENCE);

        return bean;
    }
}