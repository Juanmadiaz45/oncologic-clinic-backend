package com.oncologic.clinic.controller;

import com.oncologic.clinic.dto.registration.RegisterAdministrativeDTO;
import com.oncologic.clinic.dto.registration.RegisterDoctorDTO;
import com.oncologic.clinic.dto.registration.RegisterPatientDTO;
import com.oncologic.clinic.entity.user.User;
import com.oncologic.clinic.service.patient.PatientService;
import com.oncologic.clinic.service.personal.SpecialityService;
import com.oncologic.clinic.service.personal.AdministrativeService;
import com.oncologic.clinic.service.personal.DoctorService;
import com.oncologic.clinic.service.user.RoleService;
import com.oncologic.clinic.service.user.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final DoctorService doctorService;
    private final AdministrativeService administrativeService;
    private final PatientService patientService;
    private final RoleService roleService;
    private final SpecialityService specialityService;

    // Configuración del tamaño de página para la paginación
    private static final int PAGE_SIZE = 20;

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
    public String listUsers(
            @RequestParam("page") Optional<Integer> page,
            @RequestParam("size") Optional<Integer> size,
            @RequestParam("search") Optional<String> search,
            Model model) {

        int currentPage = page.orElse(1);
        int pageSize = size.orElse(PAGE_SIZE);
        String searchTerm = search.orElse("");

        // Crear objeto Pageable para la paginación (páginas empiezan en 0)
        Pageable pageable = PageRequest.of(currentPage - 1, pageSize);

        // Obtener página de usuarios, con o sin término de búsqueda
        Page<User> userPage;
        if (searchTerm.isEmpty()) {
            userPage = userService.getAllUsersPaginated(pageable);
        } else {
            userPage = userService.searchUsers(searchTerm, pageable);
        }

        model.addAttribute("userPage", userPage);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("searchTerm", searchTerm);

        // Calcular las páginas a mostrar en la paginación
        int totalPages = userPage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }

        return "user-list";
    }

    // Mantener los demás métodos existentes sin cambios

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
            redirectAttributes.addAttribute("successMessage", "Paciente registrado exitosamente");
            return "redirect:/users/register/patient";
        } catch (IllegalArgumentException e) {
            if (e.getMessage().contains("rol")) {
                bindingResult.rejectValue("roleIds", "roleIds.empty", e.getMessage());
            } else if (e.getMessage().contains("nombre de usuario")) {
                bindingResult.rejectValue("username", "username.duplicate", e.getMessage());
            } else {
                redirectAttributes.addAttribute("error", e.getMessage());
                return "redirect:/users/register/patient";
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
            redirectAttributes.addAttribute("successMessage", "Doctor registrado exitosamente");
            return "redirect:/users/register/doctor";
        } catch (IllegalArgumentException e) {
            if (e.getMessage().contains("rol")) {
                bindingResult.rejectValue("roleIds", "roleIds.empty", e.getMessage());
            } else if (e.getMessage().contains("nombre de usuario")) {
                bindingResult.rejectValue("username", "username.duplicate", e.getMessage());
            } else {
                redirectAttributes.addAttribute("error", e.getMessage());
                return "redirect:/users/register/doctor";
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
            redirectAttributes.addAttribute("successMessage", "Administrativo registrado exitosamente");
            return "redirect:/users/register/administrative";
        } catch (IllegalArgumentException e) {
            if (e.getMessage().contains("rol")) {
                bindingResult.rejectValue("roleIds", "roleIds.empty", e.getMessage());
            } else if (e.getMessage().contains("nombre de usuario")) {
                bindingResult.rejectValue("username", "username.duplicate", e.getMessage());
            } else {
                redirectAttributes.addAttribute("error", e.getMessage());
                return "redirect:/users/register/administrative";
            }
            model.addAttribute("roles", roleService.getAllRoles());
            return "register-administrative";
        }
    }
}