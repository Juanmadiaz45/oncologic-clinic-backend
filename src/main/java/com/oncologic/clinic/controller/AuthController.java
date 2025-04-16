package com.oncologic.clinic.controller;

import com.oncologic.clinic.dto.register.RegisterAdministrativeDTO;
import com.oncologic.clinic.dto.register.RegisterDoctorDTO;
import com.oncologic.clinic.dto.register.RegisterPatientDTO;
import com.oncologic.clinic.service.personal.AdministrativeService;
import com.oncologic.clinic.service.personal.DoctorService;
import com.oncologic.clinic.service.patient.PatientService;
import com.oncologic.clinic.service.user.RoleService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthController {

    private final AdministrativeService administrativeService;
    private final DoctorService doctorService;
    private final PatientService patientService;
    private final RoleService roleService;

    public AuthController(AdministrativeService administrativeService,
                          DoctorService doctorService,
                          PatientService patientService,
                          RoleService roleService) {
        this.administrativeService = administrativeService;
        this.doctorService = doctorService;
        this.patientService = patientService;
        this.roleService = roleService;
    }

    // Métodos GET para mostrar formularios
    @GetMapping("/users/administrative")
    public String showRegisterAdministrativeForm(Model model) {
        if (!model.containsAttribute("administrative")) {
            model.addAttribute("administrative", new RegisterAdministrativeDTO());
        }
        model.addAttribute("roles", roleService.getAllRoles());
        return "register-administrative";
    }

    @GetMapping("/users/doctor")
    public String showRegisterDoctorForm(Model model) {
        if (!model.containsAttribute("doctor")) {
            model.addAttribute("doctor", new RegisterDoctorDTO());
        }
        model.addAttribute("roles", roleService.getAllRoles());
        return "register-doctor";
    }

    @GetMapping("/users/patient")
    public String showRegisterPatientForm(Model model) {
        if (!model.containsAttribute("patient")) {
            model.addAttribute("patient", new RegisterPatientDTO());
        }
        model.addAttribute("roles", roleService.getAllRoles());
        return "register-patient";
    }

    // Registrar un Administrativo
    @PostMapping("/users/administrative")
    public String registerAdministrative(
            @ModelAttribute("administrative") RegisterAdministrativeDTO administrativeDTO,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes) {

        try {
            administrativeService.registerAdministrative(administrativeDTO);
            redirectAttributes.addFlashAttribute("successMessage", "Administrativo registrado exitosamente");
            return "redirect:/dashboard";
        } catch (IllegalArgumentException e) {
            if (e.getMessage().contains("nombre de usuario")) {
                bindingResult.rejectValue("username", "username.duplicate", e.getMessage());
            } else {
                model.addAttribute("errorMessage", e.getMessage());
            }
            model.addAttribute("roles", roleService.getAllRoles());
            return "register-administrative";
        }
    }

    // Registrar un Doctor
    @PostMapping("/users/doctor")
    public String registerDoctor(
            @ModelAttribute("doctor") RegisterDoctorDTO doctorDTO,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes) {

        try {
            doctorService.registerDoctor(doctorDTO);
            redirectAttributes.addFlashAttribute("successMessage", "Doctor registrado exitosamente");
            return "redirect:/dashboard";
        } catch (IllegalArgumentException e) {
            if (e.getMessage().contains("nombre de usuario")) {
                bindingResult.rejectValue("username", "username.duplicate", e.getMessage());
            } else {
                model.addAttribute("errorMessage", e.getMessage());
            }
            model.addAttribute("roles", roleService.getAllRoles());
            return "register-doctor";
        }
    }

    // Registrar un Paciente
    @PostMapping("/users/patient")
    public String registerPatient(
            @ModelAttribute("patient") RegisterPatientDTO patientDTO,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes) {

        try {
            patientService.registerPatient(patientDTO);
            redirectAttributes.addFlashAttribute("successMessage", "Paciente registrado exitosamente");
            return "redirect:/dashboard";
        } catch (IllegalArgumentException e) {
            if (e.getMessage().contains("nombre de usuario")) {
                bindingResult.rejectValue("username", "username.duplicate", e.getMessage());
            } else {
                model.addAttribute("errorMessage", e.getMessage());
            }
            model.addAttribute("roles", roleService.getAllRoles());
            return "register-patient";
        }
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/dashboard")
    public String home() {
        return "dashboard";
    }
}
