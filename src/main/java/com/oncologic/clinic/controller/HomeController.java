package com.oncologic.clinic.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    private static final SimpleGrantedAuthority ADMIN_AUTHORITY = new SimpleGrantedAuthority("ROLE_ADMIN");

    /**
     * Handle homepage redirects based on the user's role.
     *
     * @param authentication The user's authentication information
     * @return The appropriate redirect URL based on the user's role
     */
    @GetMapping("/")
    public String home(Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            if (authentication.getAuthorities().contains(ADMIN_AUTHORITY)) {
                return "redirect:/dashboard";
            } else {
                return "redirect:/welcome";
            }
        }
        return "redirect:/login";
    }
}