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
                .securityMatcher("/api/**")
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authRequest -> authRequest
                        // Allow static resources
                        .requestMatchers("/styles/**", "/images/**").permitAll()
                        // Allow authentication endpoints
                        .requestMatchers("/api/auth/**").permitAll()

                        // Patient module
                        .requestMatchers(HttpMethod.GET, "/api/users/patients/**").hasAnyRole("DOCTOR", "PATIENT", "ADMINISTRATIVE", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/patients/medical-history/**").hasAnyRole("DOCTOR", "PATIENT", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/patients/medical-history").hasAnyRole("DOCTOR", "ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/patients/medical-history/**").hasAnyRole("DOCTOR", "ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/patients/medical-history/**").hasAnyRole("DOCTOR", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/users/patients").hasAnyRole("DOCTOR", "ADMINISTRATIVE", "ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/users/patients/**").hasAnyRole("DOCTOR", "ADMINISTRATIVE", "ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/users/patients/**").hasAnyRole("DOCTOR", "ADMINISTRATIVE", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/appointment-results/**").hasAnyRole("DOCTOR", "PATIENT", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/appointment-results").hasAnyRole("DOCTOR", "ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/appointment-results/**").hasAnyRole("DOCTOR", "ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/appointment-results/**").hasAnyRole("DOCTOR", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/prescribed-medicines/**").hasAnyRole("DOCTOR", "PATIENT", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/prescribed-medicines").hasAnyRole("DOCTOR", "ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/prescribed-medicines/**").hasAnyRole("DOCTOR", "ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/prescribed-medicines/**").hasAnyRole("DOCTOR", "ADMIN")

                        // ADMIN has full access to ALL endpoints
                        .requestMatchers("/api/**").hasRole("ADMIN")
                        // Dashboard module - only doctors and admin
                        .requestMatchers(HttpMethod.GET, "/api/doctor-dashboard/**").hasAnyRole("DOCTOR", "ADMIN")
                        // Appointment module - only if not ADMIN
                        .requestMatchers(HttpMethod.GET, "/api/medical-appointments/*/details").hasAnyRole("DOCTOR", "ADMINISTRATIVE", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/medical-appointments/*/tasks").hasAnyRole("DOCTOR", "ADMINISTRATIVE", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/medical-appointments/*/observations").hasAnyRole("DOCTOR", "ADMINISTRATIVE", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/medical-appointments/*/treatments").hasAnyRole("DOCTOR", "ADMINISTRATIVE", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/medical-appointments/**").hasAnyRole("DOCTOR", "PATIENT", "ADMINISTRATIVE", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/medical-appointments").hasAnyRole("DOCTOR", "ADMINISTRATIVE", "ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/medical-appointments/**").hasAnyRole("DOCTOR", "ADMINISTRATIVE", "ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/medical-appointments/**").hasAnyRole("DOCTOR", "ADMINISTRATIVE", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/medical-appointments/**").hasAnyRole("DOCTOR", "PATIENT", "ADMINISTRATIVE")
                        .requestMatchers(HttpMethod.POST, "/api/medical-appointments").hasAnyRole("DOCTOR", "ADMINISTRATIVE")
                        .requestMatchers(HttpMethod.PUT, "/api/medical-appointments/**").hasAnyRole("DOCTOR", "ADMINISTRATIVE")
                        .requestMatchers(HttpMethod.DELETE, "/api/medical-appointments/**").hasAnyRole("DOCTOR", "ADMINISTRATIVE")
                        .requestMatchers(HttpMethod.GET, "/api/treatments/**").hasAnyRole("DOCTOR", "PATIENT", "ADMINISTRATIVE")
                        .requestMatchers(HttpMethod.POST, "/api/treatments").hasRole("DOCTOR")
                        .requestMatchers(HttpMethod.PUT, "/api/treatments/**").hasRole("DOCTOR")
                        .requestMatchers(HttpMethod.DELETE, "/api/treatments/**").hasRole("DOCTOR")
                        .requestMatchers(HttpMethod.GET, "/api/type-of-treatments/**").hasAnyRole("DOCTOR", "ADMINISTRATIVE")
                        .requestMatchers(HttpMethod.POST, "/api/type-of-treatments").hasRole("DOCTOR")
                        .requestMatchers(HttpMethod.PUT, "/api/type-of-treatments/**").hasRole("DOCTOR")
                        .requestMatchers(HttpMethod.DELETE, "/api/type-of-treatments/**").hasRole("DOCTOR")
                        .requestMatchers(HttpMethod.GET, "/api/observations/**").hasAnyRole("DOCTOR", "PATIENT", "ADMINISTRATIVE")
                        .requestMatchers(HttpMethod.POST, "/api/observations").hasRole("DOCTOR")
                        .requestMatchers(HttpMethod.PUT, "/api/observations/**").hasRole("DOCTOR")
                        .requestMatchers(HttpMethod.DELETE, "/api/observations/**").hasRole("DOCTOR")
                        .requestMatchers(HttpMethod.GET, "/api/medical-offices/**").hasAnyRole("DOCTOR", "ADMINISTRATIVE")
                        .requestMatchers(HttpMethod.POST, "/api/medical-offices").hasRole("ADMINISTRATIVE")
                        .requestMatchers(HttpMethod.PUT, "/api/medical-offices/**").hasRole("ADMINISTRATIVE")
                        .requestMatchers(HttpMethod.DELETE, "/api/medical-offices/**").hasRole("ADMINISTRATIVE")
                        .requestMatchers(HttpMethod.GET, "/api/medical-tasks/**").hasAnyRole("DOCTOR", "ADMINISTRATIVE")
                        .requestMatchers(HttpMethod.POST, "/api/medical-tasks").hasAnyRole("DOCTOR", "ADMINISTRATIVE")
                        .requestMatchers(HttpMethod.PUT, "/api/medical-tasks/**").hasAnyRole("DOCTOR", "ADMINISTRATIVE")
                        .requestMatchers(HttpMethod.DELETE, "/api/medical-tasks/**").hasAnyRole("DOCTOR", "ADMINISTRATIVE")
                        .requestMatchers(HttpMethod.GET, "/api/medical-appointment-types/**").hasAnyRole("DOCTOR", "ADMINISTRATIVE")
                        .requestMatchers(HttpMethod.POST, "/api/medical-appointment-types").hasRole("ADMINISTRATIVE")
                        .requestMatchers(HttpMethod.PUT, "/api/medical-appointment-types/**").hasRole("ADMINISTRATIVE")
                        .requestMatchers(HttpMethod.DELETE, "/api/medical-appointment-types/**").hasRole("ADMINISTRATIVE")

                        // Availability module
                        .requestMatchers(HttpMethod.GET, "/api/availabilities/**").hasAnyRole("DOCTOR", "ADMINISTRATIVE")
                        .requestMatchers(HttpMethod.POST, "/api/availabilities").hasRole("ADMINISTRATIVE")
                        .requestMatchers(HttpMethod.PUT, "/api/availabilities/**").hasRole("ADMINISTRATIVE")
                        .requestMatchers(HttpMethod.DELETE, "/api/availabilities/**").hasRole("ADMINISTRATIVE")
                        .requestMatchers(HttpMethod.GET, "/api/statuses/**").hasAnyRole("DOCTOR", "ADMINISTRATIVE")
                        .requestMatchers(HttpMethod.POST, "/api/statuses").hasRole("ADMINISTRATIVE")
                        .requestMatchers(HttpMethod.PUT, "/api/statuses/**").hasRole("ADMINISTRATIVE")
                        .requestMatchers(HttpMethod.DELETE, "/api/statuses/**").hasRole("ADMINISTRATIVE")

                        // Examination module
                        .requestMatchers(HttpMethod.GET, "/api/examination-results/**").hasAnyRole("DOCTOR", "LAB_TECHNICIAN", "PATIENT")
                        .requestMatchers(HttpMethod.POST, "/api/examination-results").hasAnyRole("DOCTOR", "LAB_TECHNICIAN")
                        .requestMatchers(HttpMethod.PUT, "/api/examination-results/**").hasAnyRole("DOCTOR", "LAB_TECHNICIAN")
                        .requestMatchers(HttpMethod.DELETE, "/api/examination-results/**").hasAnyRole("DOCTOR", "LAB_TECHNICIAN")
                        .requestMatchers(HttpMethod.GET, "/api/laboratories/**").hasAnyRole("DOCTOR", "LAB_TECHNICIAN")
                        .requestMatchers(HttpMethod.POST, "/api/laboratories").hasRole("LAB_TECHNICIAN")
                        .requestMatchers(HttpMethod.PUT, "/api/laboratories/**").hasRole("LAB_TECHNICIAN")
                        .requestMatchers(HttpMethod.DELETE, "/api/laboratories/**").hasRole("LAB_TECHNICIAN")
                        .requestMatchers(HttpMethod.GET, "/api/medical-examinations/**").hasAnyRole("DOCTOR", "LAB_TECHNICIAN", "PATIENT")
                        .requestMatchers(HttpMethod.POST, "/api/medical-examinations").hasAnyRole("DOCTOR", "LAB_TECHNICIAN")
                        .requestMatchers(HttpMethod.PUT, "/api/medical-examinations/**").hasAnyRole("DOCTOR", "LAB_TECHNICIAN")
                        .requestMatchers(HttpMethod.DELETE, "/api/medical-examinations/**").hasAnyRole("DOCTOR", "LAB_TECHNICIAN")
                        .requestMatchers(HttpMethod.GET, "/api/types-of-exam/**").hasAnyRole("DOCTOR", "LAB_TECHNICIAN")
                        .requestMatchers(HttpMethod.POST, "/api/types-of-exam").hasRole("LAB_TECHNICIAN")
                        .requestMatchers(HttpMethod.PUT, "/api/types-of-exam/**").hasRole("LAB_TECHNICIAN")
                        .requestMatchers(HttpMethod.DELETE, "/api/types-of-exam/**").hasRole("LAB_TECHNICIAN")


                        // Personal module
                        .requestMatchers(HttpMethod.GET, "/api/administrative/**").hasRole("ADMINISTRATIVE")
                        .requestMatchers(HttpMethod.POST, "/api/administrative").hasRole("ADMINISTRATIVE")
                        .requestMatchers(HttpMethod.PUT, "/api/administrative/**").hasRole("ADMINISTRATIVE")
                        .requestMatchers(HttpMethod.DELETE, "/api/administrative/**").hasRole("ADMINISTRATIVE")
                        .requestMatchers(HttpMethod.GET, "/api/doctors/**").hasRole("DOCTOR")
                        .requestMatchers(HttpMethod.POST, "/api/doctors").denyAll() // Only ADMIN can create doctors
                        .requestMatchers(HttpMethod.PUT, "/api/doctors/**").denyAll() // Only ADMIN can update doctors
                        .requestMatchers(HttpMethod.DELETE, "/api/doctors/**").denyAll() // Only ADMIN can delete doctors
                        .requestMatchers(HttpMethod.GET, "/api/specialities/**").hasRole("DOCTOR")
                        .requestMatchers(HttpMethod.POST, "/api/specialities").denyAll() // Only ADMIN
                        .requestMatchers(HttpMethod.PUT, "/api/specialities/**").denyAll() // Only ADMIN
                        .requestMatchers(HttpMethod.DELETE, "/api/specialities/**").denyAll() // Only ADMIN

                        // User module - Only ADMIN can access
                        .requestMatchers("/api/permissions/**").denyAll()
                        .requestMatchers("/api/roles/**").denyAll()
                        .requestMatchers(HttpMethod.GET, "/api/users/**").hasAnyRole("DOCTOR", "PATIENT")
                        .requestMatchers(HttpMethod.POST, "/api/users").denyAll()
                        .requestMatchers(HttpMethod.PUT, "/api/users/**").denyAll()
                        .requestMatchers(HttpMethod.DELETE, "/api/users/**").denyAll()
                        // Any other request must be authenticated
                        .anyRequest().authenticated())
                .sessionManagement(sessionManager -> sessionManager.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
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
