package com.oncologic.clinic.config;

import com.oncologic.clinic.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Collections;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final AuthenticationProvider authenticationProvider;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    /**
     * Defines the security filter chain that will be applied to all REST requests.
     */
    @Bean
    @Order(1)
    public SecurityFilterChain jwtSecurityFilterChain(HttpSecurity http) throws Exception {
        return http
                // Apply all security rules to all requests (except for static resources)
                .securityMatcher("/api/**")
                // Disable CSRF protection as it will be handled via JWT tokens
                .csrf(AbstractHttpConfigurer::disable)
                // Disable CORS as it will be manually configured with a separate filter
                // Configuration of HTTP request authorization rules
                .authorizeHttpRequests(authRequest -> authRequest
                        // Allow access to static resources
                        .requestMatchers("/styles/**", "/images/**").permitAll()
                        // Allow all requests starting with /auth, such as login or registration
                        .requestMatchers("/api/auth/**").permitAll()
                        // Appointment module
                        .requestMatchers(HttpMethod.GET, "/api/medical-appointments/**").hasAnyRole("ADMIN", "DOCTOR", "PATIENT", "ADMINISTRATIVE")
                        .requestMatchers(HttpMethod.POST, "/api/medical-appointments").hasAnyRole("ADMIN", "DOCTOR", "ADMINISTRATIVE")
                        .requestMatchers(HttpMethod.PUT, "/api/medical-appointments/**").hasAnyRole("ADMIN", "DOCTOR", "ADMINISTRATIVE")
                        .requestMatchers(HttpMethod.DELETE, "/api/medical-appointments/**").hasAnyRole("ADMIN", "DOCTOR", "ADMINISTRATIVE")
                        .requestMatchers(HttpMethod.GET, "/api/medical-offices/**").hasAnyRole("ADMIN", "DOCTOR", "ADMINISTRATIVE")
                        .requestMatchers(HttpMethod.POST, "/api/medical-offices").hasAnyRole("ADMIN", "ADMINISTRATIVE")
                        .requestMatchers(HttpMethod.PUT, "/api/medical-offices/**").hasAnyRole("ADMIN", "ADMINISTRATIVE")
                        .requestMatchers(HttpMethod.DELETE, "/api/medical-offices/**").hasAnyRole("ADMIN", "ADMINISTRATIVE")
                        .requestMatchers(HttpMethod.GET, "/api/medical-tasks/**").hasAnyRole("ADMIN", "DOCTOR", "ADMINISTRATIVE")
                        .requestMatchers(HttpMethod.POST, "/api/medical-tasks").hasAnyRole("ADMIN", "DOCTOR", "ADMINISTRATIVE")
                        .requestMatchers(HttpMethod.PUT, "/api/medical-tasks/**").hasAnyRole("ADMIN", "DOCTOR", "ADMINISTRATIVE")
                        .requestMatchers(HttpMethod.DELETE, "/api/medical-tasks/**").hasAnyRole("ADMIN", "DOCTOR", "ADMINISTRATIVE")
                        .requestMatchers(HttpMethod.GET, "/api/medical-appointment-types/**").hasAnyRole("ADMIN", "DOCTOR", "ADMINISTRATIVE")
                        .requestMatchers(HttpMethod.POST, "/api/medical-appointment-types").hasAnyRole("ADMIN", "ADMINISTRATIVE")
                        .requestMatchers(HttpMethod.PUT, "/api/medical-appointment-types/**").hasAnyRole("ADMIN", "ADMINISTRATIVE")
                        .requestMatchers(HttpMethod.DELETE, "/api/medical-appointment-types/**").hasAnyRole("ADMIN", "ADMINISTRATIVE")
                        // Availability module
                        .requestMatchers(HttpMethod.GET, "/api/availabilities/**").hasAnyRole("ADMIN", "DOCTOR", "ADMINISTRATIVE")
                        .requestMatchers(HttpMethod.POST, "/api/availabilities").hasAnyRole("ADMIN", "ADMINISTRATIVE")
                        .requestMatchers(HttpMethod.PUT, "/api/availabilities/**").hasAnyRole("ADMIN", "ADMINISTRATIVE")
                        .requestMatchers(HttpMethod.DELETE, "/api/availabilities/**").hasAnyRole("ADMIN", "ADMINISTRATIVE")
                        .requestMatchers(HttpMethod.GET, "/api/statuses/**").hasAnyRole("ADMIN", "DOCTOR", "ADMINISTRATIVE")
                        .requestMatchers(HttpMethod.POST, "/api/statuses").hasAnyRole("ADMIN", "ADMINISTRATIVE")
                        .requestMatchers(HttpMethod.PUT, "/api/statuses/**").hasAnyRole("ADMIN", "ADMINISTRATIVE")
                        .requestMatchers(HttpMethod.DELETE, "/api/statuses/**").hasAnyRole("ADMIN", "ADMINISTRATIVE")
                        // Examination module
                        .requestMatchers(HttpMethod.GET, "/api/examination-results/**").hasAnyRole("ADMIN", "DOCTOR", "LAB_TECHNICIAN", "PATIENT")
                        .requestMatchers(HttpMethod.POST, "/api/examination-results").hasAnyRole("ADMIN", "DOCTOR", "LAB_TECHNICIAN")
                        .requestMatchers(HttpMethod.PUT, "/api/examination-results/**").hasAnyRole("ADMIN", "DOCTOR", "LAB_TECHNICIAN")
                        .requestMatchers(HttpMethod.DELETE, "/api/examination-results/**").hasAnyRole("ADMIN", "DOCTOR", "LAB_TECHNICIAN")
                        .requestMatchers(HttpMethod.GET, "/api/laboratories/**").hasAnyRole("ADMIN", "DOCTOR", "LAB_TECHNICIAN")
                        .requestMatchers(HttpMethod.POST, "/api/laboratories").hasAnyRole("ADMIN", "LAB_TECHNICIAN")
                        .requestMatchers(HttpMethod.PUT, "/api/laboratories/**").hasAnyRole("ADMIN", "LAB_TECHNICIAN")
                        .requestMatchers(HttpMethod.DELETE, "/api/laboratories/**").hasAnyRole("ADMIN", "LAB_TECHNICIAN")
                        .requestMatchers(HttpMethod.GET, "/api/medical-examinations/**").hasAnyRole("ADMIN", "DOCTOR", "LAB_TECHNICIAN", "PATIENT")
                        .requestMatchers(HttpMethod.POST, "/api/medical-examinations").hasAnyRole("ADMIN", "DOCTOR", "LAB_TECHNICIAN")
                        .requestMatchers(HttpMethod.PUT, "/api/medical-examinations/**").hasAnyRole("ADMIN", "DOCTOR", "LAB_TECHNICIAN")
                        .requestMatchers(HttpMethod.DELETE, "/api/medical-examinations/**").hasAnyRole("ADMIN", "DOCTOR", "LAB_TECHNICIAN")
                        .requestMatchers(HttpMethod.GET, "/api/types-of-exam/**").hasAnyRole("ADMIN", "DOCTOR", "LAB_TECHNICIAN")
                        .requestMatchers(HttpMethod.POST, "/api/types-of-exam").hasAnyRole("ADMIN", "LAB_TECHNICIAN")
                        .requestMatchers(HttpMethod.PUT, "/api/types-of-exam/**").hasAnyRole("ADMIN", "LAB_TECHNICIAN")
                        .requestMatchers(HttpMethod.DELETE, "/api/types-of-exam/**").hasAnyRole("ADMIN", "LAB_TECHNICIAN")
                        // Patient module
                        .requestMatchers(HttpMethod.GET, "/api/appointment-results/**").hasAnyRole("ADMIN", "DOCTOR", "PATIENT")
                        .requestMatchers(HttpMethod.POST, "/api/appointment-results").hasAnyRole("ADMIN", "DOCTOR")
                        .requestMatchers(HttpMethod.PUT, "/api/appointment-results/**").hasAnyRole("ADMIN", "DOCTOR")
                        .requestMatchers(HttpMethod.DELETE, "/api/appointment-results/**").hasAnyRole("ADMIN", "DOCTOR")
                        .requestMatchers(HttpMethod.GET, "/api/patients/medical-history/**").hasAnyRole("ADMIN", "DOCTOR", "PATIENT")
                        .requestMatchers(HttpMethod.POST, "/api/patients/medical-history").hasAnyRole("ADMIN", "DOCTOR")
                        .requestMatchers(HttpMethod.PUT, "/api/patients/medical-history/**").hasAnyRole("ADMIN", "DOCTOR")
                        .requestMatchers(HttpMethod.DELETE, "/api/patients/medical-history/**").hasAnyRole("ADMIN", "DOCTOR")
                        .requestMatchers(HttpMethod.GET, "/api/observations/**").hasAnyRole("ADMIN", "DOCTOR", "PATIENT")
                        .requestMatchers(HttpMethod.POST, "/api/observations").hasAnyRole("ADMIN", "DOCTOR")
                        .requestMatchers(HttpMethod.PUT, "/api/observations/**").hasAnyRole("ADMIN", "DOCTOR")
                        .requestMatchers(HttpMethod.DELETE, "/api/observations/**").hasAnyRole("ADMIN", "DOCTOR")
                        .requestMatchers(HttpMethod.GET, "/api/users/patients/**").hasAnyRole("ADMIN", "DOCTOR", "PATIENT", "ADMINISTRATIVE")
                        .requestMatchers(HttpMethod.POST, "/api/users/patients").hasAnyRole("ADMIN", "DOCTOR", "ADMINISTRATIVE")
                        .requestMatchers(HttpMethod.PUT, "/api/users/patients/**").hasAnyRole("ADMIN", "DOCTOR", "ADMINISTRATIVE")
                        .requestMatchers(HttpMethod.DELETE, "/api/users/patients/**").hasAnyRole("ADMIN", "DOCTOR", "ADMINISTRATIVE")
                        .requestMatchers(HttpMethod.GET, "/api/prescribed-medicines/**").hasAnyRole("ADMIN", "DOCTOR", "PATIENT")
                        .requestMatchers(HttpMethod.POST, "/api/prescribed-medicines").hasAnyRole("ADMIN", "DOCTOR")
                        .requestMatchers(HttpMethod.PUT, "/api/prescribed-medicines/**").hasAnyRole("ADMIN", "DOCTOR")
                        .requestMatchers(HttpMethod.DELETE, "/api/prescribed-medicines/**").hasAnyRole("ADMIN", "DOCTOR")
                        .requestMatchers(HttpMethod.GET, "/api/treatments/**").hasAnyRole("ADMIN", "DOCTOR", "PATIENT")
                        .requestMatchers(HttpMethod.POST, "/api/treatments").hasAnyRole("ADMIN", "DOCTOR")
                        .requestMatchers(HttpMethod.PUT, "/api/treatments/**").hasAnyRole("ADMIN", "DOCTOR")
                        .requestMatchers(HttpMethod.DELETE, "/api/treatments/**").hasAnyRole("ADMIN", "DOCTOR")
                        .requestMatchers(HttpMethod.GET, "/api/type-of-treatments/**").hasAnyRole("ADMIN", "DOCTOR")
                        .requestMatchers(HttpMethod.POST, "/api/type-of-treatments").hasAnyRole("ADMIN", "DOCTOR")
                        .requestMatchers(HttpMethod.PUT, "/api/type-of-treatments/**").hasAnyRole("ADMIN", "DOCTOR")
                        .requestMatchers(HttpMethod.DELETE, "/api/type-of-treatments/**").hasAnyRole("ADMIN", "DOCTOR")
                        // Personal module
                        .requestMatchers(HttpMethod.GET, "/api/administrative/**").hasAnyRole("ADMIN", "ADMINISTRATIVE")
                        .requestMatchers(HttpMethod.POST, "/api/administrative").hasAnyRole("ADMIN", "ADMINISTRATIVE")
                        .requestMatchers(HttpMethod.PUT, "/api/administrative/**").hasAnyRole("ADMIN", "ADMINISTRATIVE")
                        .requestMatchers(HttpMethod.DELETE, "/api/administrative/**").hasAnyRole("ADMIN", "ADMINISTRATIVE")
                        .requestMatchers(HttpMethod.GET, "/api/doctors/**").hasAnyRole("ADMIN", "DOCTOR")
                        .requestMatchers(HttpMethod.POST, "/api/doctors").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/doctors/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/doctors/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/specialities/**").hasAnyRole("ADMIN", "DOCTOR")
                        .requestMatchers(HttpMethod.POST, "/api/specialities").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/specialities/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/specialities/**").hasRole("ADMIN")
                        // User module
                        .requestMatchers("/api/permissions/**").hasRole("ADMIN")
                        .requestMatchers("/api/roles/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/users/**").hasAnyRole("ADMIN", "DOCTOR", "PATIENT")
                        .requestMatchers(HttpMethod.POST, "/api/users").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/users/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/users/**").hasRole("ADMIN")
                        // Allow SSE connections
                        // Any other request must be authenticated
                        .anyRequest().authenticated())
                // Set the session management policy to STATELESS (no server-side session storage)
                .sessionManagement(sessionManager -> sessionManager.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // Configure the authentication provider that validates credentials
                .authenticationProvider(authenticationProvider)
                // Configure exception handling to return JSON error responses when authentication fails or access is denied
                .exceptionHandling(
                        eh ->
                                eh.authenticationEntryPoint((request, response, authException) -> {
                                    response.setStatus(401);
                                    response.setContentType("application/json");
                                    response.getWriter().write("{\"error\": \"Authentication is required to access this resource\"}");
                                }).accessDeniedHandler((request, response, accessDeniedException) -> {
                                    response.setStatus(403);
                                    response.setContentType("application/json");
                                    response.getWriter().write("{\"error\": \"You don't have permission to access this resource\"}");
                                }))
                // Add a JWT authentication filter before the username/password authentication filter
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class).build();
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

        // Register configuration for all routes in api (/api/**)
        source.registerCorsConfiguration("/api/**", config);

        // Register filter with defined configuration and set the highest priority
        FilterRegistrationBean<?> bean = new FilterRegistrationBean<>(new CorsFilter(source));
        bean.setOrder(Ordered.HIGHEST_PRECEDENCE);

        return bean;
    }

    @Bean
    @Order(2)
    public SecurityFilterChain mvcSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                // Apply all security rules to all requests (except for static resources)
                .securityMatcher("/**")
                // Configuration of HTTP request authorization rules
                .authorizeHttpRequests(auth -> auth.requestMatchers("/", "/login", "/css/**", "/js/**", "/images/**").permitAll().requestMatchers(
                        "/swagger-ui/**",
                        "/v3/api-docs/**",
                        "/swagger-ui.html",
                        "/swagger-resources/**",
                        "/webjars/**").permitAll().requestMatchers("/users/**", "/roles/**", "/dashboard/**").hasRole("ADMIN").requestMatchers("/patient/registry", "/patient/create", "/patient/new").hasAnyRole("ADMIN", "DOCTOR", "ADMINISTRATIVE").requestMatchers("/administrative/**").hasAnyRole("ADMIN", "ADMINISTRATIVE").requestMatchers("/doctor/**").hasAnyRole("ADMIN", "DOCTOR").anyRequest().authenticated())
                // Configure the login form
                .formLogin(login -> login.loginPage("/login").successHandler(successHandler()).permitAll())
                // Configure logout
                .logout(logout -> logout.logoutRequestMatcher(new AntPathRequestMatcher("/logout")).logoutSuccessUrl("/login?logout").permitAll())
                // Configure exception handling
                .exceptionHandling(eh -> eh.authenticationEntryPoint((request, response, authException) -> {
                    // Redirects to log in if not authenticated
                    response.sendRedirect(request.getContextPath() + "/login");
                }).accessDeniedHandler((request, response, accessDeniedException) -> {
                    // Redirects to an access-denied page
                    response.sendRedirect(request.getContextPath() + "/denied");
                }));

        return http.build();
    }

    @Bean
    public AuthenticationSuccessHandler successHandler() {
        return (request, response, authentication) -> {
            String contextPath = request.getContextPath();

            boolean isAdmin = authentication.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));

            if (isAdmin) response.sendRedirect(contextPath + "/dashboard");
            else response.sendRedirect(contextPath + "/welcome");

        };
    }
}
