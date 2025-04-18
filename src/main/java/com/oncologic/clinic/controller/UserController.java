package com.oncologic.clinic.controller;

import com.oncologic.clinic.dto.info.UserListGroupedDTO;
import com.oncologic.clinic.dto.registration.RegisterAdministrativeDTO;
import com.oncologic.clinic.dto.registration.RegisterDoctorDTO;
import com.oncologic.clinic.dto.registration.RegisterPatientDTO;
import com.oncologic.clinic.service.patient.PatientService;
import com.oncologic.clinic.service.personal.SpecialityService;
import com.oncologic.clinic.service.personal.AdministrativeService;
import com.oncologic.clinic.service.personal.DoctorService;
import com.oncologic.clinic.service.user.RoleService;
import com.oncologic.clinic.service.user.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final DoctorService doctorService;
    private final AdministrativeService administrativeService;
    private final PatientService patientService;
    private final RoleService roleService;
    private final SpecialityService specialityService;

    public UserController(UserService userService,
                          DoctorService doctorService,
                          AdministrativeService administrativeService,
                          PatientService patientService,
                          RoleService roleService,
                          SpecialityService specialityService) {
        this.userService = userService;
        this.doctorService = doctorService;
        this.administrativeService = administrativeService;
        this.patientService = patientService;
        this.roleService = roleService;
        this.specialityService = specialityService;
    }

    @GetMapping
    public String getGroupedUsers(Model model) {
        UserListGroupedDTO groupedUsers = userService.listUsersGroupedByType();
        model.addAttribute("doctors", groupedUsers.getDoctors());
        model.addAttribute("administratives", groupedUsers.getAdministratives());
        model.addAttribute("patients", groupedUsers.getPatients());
        return "user-list";
    }

    @GetMapping("/register/patient")
    public String formPatient(Model model) {
        if (!model.containsAttribute("patient")) {
            model.addAttribute("patient", new RegisterPatientDTO());
        }
        model.addAttribute("roles", roleService.getAllRoles());
        return "register-patient";
    }

    @GetMapping("/register/doctor")
    public String formDoctor(Model model) {
        if (!model.containsAttribute("doctor")) {
            model.addAttribute("doctor", new RegisterDoctorDTO());
        }
        model.addAttribute("roles", roleService.getAllRoles());
        model.addAttribute("specialities", specialityService.getAllSpecialities());
        return "register-doctor";
    }

    @GetMapping("/register/administrative")
    public String formAdministrative(Model model) {
        if (!model.containsAttribute("administrative")) {
            model.addAttribute("administrative", new RegisterAdministrativeDTO());
        }
        model.addAttribute("roles", roleService.getAllRoles());
        return "register-administrative";
    }

    @PostMapping("/register/patient")
    public String registerPatient(@ModelAttribute("patient") RegisterPatientDTO patientDTO,
                                    BindingResult bindingResult,
                                    Model model,
                                    RedirectAttributes redirectAttributes) {
        try {
            patientService.registerPatient(patientDTO);
            redirectAttributes.addFlashAttribute("successMessage", "Paciente registrado exitosamente");
            return "redirect:/dashboard";
        } catch (IllegalArgumentException e) {
            if (e.getMessage().contains("rol")) {
                bindingResult.rejectValue("roleIds", "roleIds.empty", e.getMessage());
            } else if (e.getMessage().contains("nombre de usuario")) {
                bindingResult.rejectValue("username", "username.duplicate", e.getMessage());
            } else {
                model.addAttribute("errorMessage", e.getMessage());
            }
            model.addAttribute("roles", roleService.getAllRoles());
            return "register-patient";
        }
    }

    @PostMapping("/register/doctor")
    public String registerDoctor(@ModelAttribute("doctor") RegisterDoctorDTO doctorDTO,
                                  BindingResult bindingResult,
                                  Model model,
                                  RedirectAttributes redirectAttributes) {
        try {
            doctorService.registerDoctor(doctorDTO);
            redirectAttributes.addFlashAttribute("successMessage", "Doctor registrado exitosamente");
            return "redirect:/dashboard";
        } catch (IllegalArgumentException e) {
            if (e.getMessage().contains("rol")) {
                bindingResult.rejectValue("roleIds", "roleIds.empty", e.getMessage());
            } else if (e.getMessage().contains("nombre de usuario")) {
                bindingResult.rejectValue("username", "username.duplicate", e.getMessage());
            } else {
                model.addAttribute("errorMessage", e.getMessage());
            }
            model.addAttribute("roles", roleService.getAllRoles());
            model.addAttribute("specialities", specialityService.getAllSpecialities());
            return "register-doctor";
        }
    }

    @PostMapping("/register/administrative")
    public String registerAdministrative(@ModelAttribute("administrative") RegisterAdministrativeDTO administrativeDTO,
                                          BindingResult bindingResult,
                                          Model model,
                                          RedirectAttributes redirectAttributes) {
        try {
            administrativeService.registerAdministrative(administrativeDTO);
            redirectAttributes.addFlashAttribute("successMessage", "Administrativo registrado exitosamente");
            return "redirect:/dashboard";
        } catch (IllegalArgumentException e) {
            if (e.getMessage().contains("rol")) {
                bindingResult.rejectValue("roleIds", "roleIds.empty", e.getMessage());
            } else if (e.getMessage().contains("nombre de usuario")) {
                bindingResult.rejectValue("username", "username.duplicate", e.getMessage());
            } else {
                model.addAttribute("errorMessage", e.getMessage());
            }
            model.addAttribute("roles", roleService.getAllRoles());
            return "register-administrative";
        }
    }
}
