package com.oncologic.clinic.controller;

import com.oncologic.clinic.dto.personal.request.SpecialityRequestDTO;
import com.oncologic.clinic.service.personal.SpecialityService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/specialities")
public class SpecialityController {
    private final SpecialityService specialityService;

    public SpecialityController(SpecialityService specialityService) {
        this.specialityService = specialityService;
    }

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        if (!model.containsAttribute("speciality")) {
            model.addAttribute("speciality", new SpecialityRequestDTO());
        }
        return "register-speciality";
    }

    @PostMapping("/register")
    public String registerSpeciality(
            @ModelAttribute("speciality") @Valid SpecialityRequestDTO specialityDTO,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.speciality", bindingResult);
            redirectAttributes.addFlashAttribute("speciality", specialityDTO);
            return "redirect:/specialities/register";
        }

        try {
            specialityService.createSpeciality(specialityDTO);
            redirectAttributes.addFlashAttribute("successMessage", "Especialidad registrada exitosamente");
            return "redirect:/dashboard";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al registrar la especialidad: " + e.getMessage());
            return "redirect:/specialities/register";
        }
    }
}
