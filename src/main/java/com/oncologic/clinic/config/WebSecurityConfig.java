package com.oncologic.clinic.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Bean
    @Order(2)
    public SecurityFilterChain mvcSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                // Apply all security rules to all requests (except for static resources)
                .securityMatcher("/**")
                // Configuration of HTTP request authorization rules
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/login", "/css/**", "/js/**", "/images/**").permitAll()
                        .requestMatchers("/users/**", "/roles/**", "/dashboard/**").hasRole("ADMIN")
                        .requestMatchers("/patient/registry", "/patient/create", "/patient/new")
                        .hasAnyRole("ADMIN", "DOCTOR", "ADMINISTRATIVE")
                        .requestMatchers("/administrative/**").hasAnyRole("ADMIN", "ADMINISTRATIVE")
                        .requestMatchers("/doctor/**").hasAnyRole("ADMIN", "DOCTOR")
                        .anyRequest().authenticated()
                )
                // Configure the login form
                .formLogin(login -> login
                        .loginPage("/login")
                        .successHandler(successHandler())
                        .permitAll()
                )
                // Configure logout
                .logout(logout -> logout
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                )
                // Configure exception handling
                .exceptionHandling(eh -> eh
                        .authenticationEntryPoint((request, response, authException) -> {
                            // Redirects to log in if not authenticated
                            response.sendRedirect("/login");
                        })
                        .accessDeniedHandler((request, response, accessDeniedException) -> {
                            // Redirects to an access-denied page
                            response.sendRedirect("/denied");
                        })
                );

        return http.build();
    }

    @Bean
    public AuthenticationSuccessHandler successHandler() {
        return (request, response, authentication) -> {
            String contextPath = request.getContextPath();

            boolean isAdmin = authentication.getAuthorities().stream()
                    .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));

            if (isAdmin)
                response.sendRedirect(contextPath + "/dashboard");
            else
                response.sendRedirect(contextPath + "/welcome");

        };
    }
}
