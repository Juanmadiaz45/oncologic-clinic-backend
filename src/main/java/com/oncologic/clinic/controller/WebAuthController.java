package com.oncologic.clinic.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebAuthController {
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/dashboard")
    public String dashboard() {
        return "dashboard";
    }

    @GetMapping("/welcome")
    public String welcomeUser() {
        return "welcome";
    }

    @GetMapping("/denied")
    public String deniedAccess() {
        return "denied";
    }
}
