package com.oncologic.clinic.service.personal.impl;

import com.oncologic.clinic.dto.personal.DoctorDTO;
import com.oncologic.clinic.dto.personal.request.PersonalRequestDTO;
import com.oncologic.clinic.dto.personal.response.DoctorResponseDTO;
import com.oncologic.clinic.dto.registration.RegisterDoctorDTO;
import com.oncologic.clinic.dto.user.UserDTO;
import com.oncologic.clinic.dto.user.response.UserResponseDTO;
import com.oncologic.clinic.entity.personal.Doctor;
import com.oncologic.clinic.entity.personal.Speciality;
import com.oncologic.clinic.entity.user.User;
import com.oncologic.clinic.mapper.personal.DoctorMapper;
import com.oncologic.clinic.repository.personal.DoctorRepository;
import com.oncologic.clinic.repository.personal.SpecialityRepository;
import com.oncologic.clinic.service.personal.DoctorService;
import com.oncologic.clinic.service.user.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class DoctorServiceImpl implements DoctorService {
    private final DoctorRepository doctorRepository;
    private final UserService userService;
    private final SpecialityRepository specialityRepository;
    private final DoctorMapper doctorMapper;

    DoctorServiceImpl(DoctorRepository doctorRepository, UserService userService, SpecialityRepository specialityRepository, DoctorMapper doctorMapper) {
        this.doctorRepository = doctorRepository;
        this.userService = userService;
        this.specialityRepository = specialityRepository;
        this.doctorMapper = doctorMapper;
    }

    @Override
    public DoctorResponseDTO getDoctorById(Long id) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Doctor con ID " + id + " no encontrado"));

        return doctorMapper.toDto(doctor);
    }

    @Override
    public List<DoctorResponseDTO> getAllDoctors() {
        return doctorRepository.findAll().stream().map(doctorMapper::toDto).toList();
    }

    @Override
    @Transactional
    public Doctor registerDoctor(RegisterDoctorDTO doctorDTO) {
        User user = userService.createUser(doctorDTO);

        Doctor doctor = new Doctor();
        doctor.setUser(user);
        doctor.setIdNumber(doctorDTO.getIdNumber());
        doctor.setName(doctorDTO.getName());
        doctor.setLastName(doctorDTO.getLastname());
        doctor.setEmail(doctorDTO.getEmail());
        doctor.setPhoneNumber(doctorDTO.getPhoneNumber());
        doctor.setMedicalLicenseNumber(doctorDTO.getMedicalLicenseNumber());
        doctor.setDateOfHiring(LocalDateTime.now());
        doctor.setStatus('A');

        Doctor savedDoctor = doctorRepository.save(doctor);

        if (doctorDTO.getSpecialityIds() != null && !doctorDTO.getSpecialityIds().isEmpty()) {
            assignSpecialitiesToDoctor(savedDoctor, doctorDTO.getSpecialityIds());
        }

        return savedDoctor;
    }

    private void assignSpecialitiesToDoctor(Doctor doctor, Set<Long> specialityIds) {
        Set<Speciality> newSpecialities = specialityRepository.findAllById(specialityIds)
                .stream()
                .collect(Collectors.toSet());

        Set<Speciality> currentSpecialities = new HashSet<>(doctor.getSpecialities());

        Set<Speciality> toRemove = currentSpecialities.stream()
                .filter(spec -> !newSpecialities.contains(spec))
                .collect(Collectors.toSet());

        toRemove.forEach(spec -> {
            spec.getDoctors().remove(doctor);
            doctor.getSpecialities().remove(spec);
        });

        newSpecialities.forEach(spec -> {
            if (!currentSpecialities.contains(spec)) {
                if (spec.getDoctors() == null) {
                    spec.setDoctors(new HashSet<>());
                }
                spec.getDoctors().add(doctor);
                doctor.getSpecialities().add(spec);
            }
        });
    }

    @Override
    @Transactional
    public DoctorResponseDTO createDoctor(DoctorDTO doctorDTO) {
        if (doctorDTO.getMedicalLicenseNumber() == null || doctorDTO.getMedicalLicenseNumber().isEmpty()) {
            throw new IllegalArgumentException("El número de licencia médica no puede estar vacío");
        }

        RegisterDoctorDTO registerDto = new RegisterDoctorDTO();

        if (doctorDTO.getPersonalData() != null) {
            PersonalRequestDTO personalData = doctorDTO.getPersonalData();
            registerDto.setIdNumber(personalData.getIdNumber());
            registerDto.setName(personalData.getName());
            registerDto.setLastname(personalData.getLastName());
            registerDto.setEmail(personalData.getEmail());
            registerDto.setPhoneNumber(personalData.getPhoneNumber());

            if (personalData.getUserData() != null) {
                UserDTO userData = personalData.getUserData();
                registerDto.setUsername(userData.getUsername());
                registerDto.setPassword(userData.getPassword());
                registerDto.setRoleIds(userData.getRoleIds());
            }
        }

        registerDto.setMedicalLicenseNumber(doctorDTO.getMedicalLicenseNumber());
        registerDto.setSpecialityIds(doctorDTO.getSpecialityIds());


        Doctor doctor = registerDoctor(registerDto);

        return doctorMapper.toDto(doctor);
    }


    @Override
    @Transactional
    public DoctorResponseDTO updateDoctor(Long id, DoctorDTO doctorDTO) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Doctor con el ID " + id + " no encontrado"));

        doctorMapper.updateEntityFromDto(doctorDTO, doctor);

        // Si vienen especialidades nuevas
        if (doctorDTO.getSpecialityIds() != null) {
            // 1. Quitar doctor de especialidades antiguas que ya no están
            Set<Speciality> currentSpecialities = doctor.getSpecialities() != null
                    ? new HashSet<>(doctor.getSpecialities())
                    : new HashSet<>();

            for (Speciality oldSpeciality : currentSpecialities) {
                if (!doctorDTO.getSpecialityIds().contains(oldSpeciality.getId())) {
                    oldSpeciality.getDoctors().remove(doctor);
                    specialityRepository.save(oldSpeciality);
                }
            }

            // 2. Agregar doctor a nuevas especialidades
            Set<Speciality> updatedSpecialities = new HashSet<>();
            for (Long specialityId : doctorDTO.getSpecialityIds()) {
                Speciality speciality = specialityRepository.findById(specialityId)
                        .orElseThrow(() -> new EntityNotFoundException("Especialidad con el ID " + specialityId + " no encontrada"));
                speciality.getDoctors().add(doctor);
                updatedSpecialities.add(speciality);
            }
            specialityRepository.saveAll(updatedSpecialities);
            doctor.setSpecialities(updatedSpecialities);
        }

        return doctorMapper.toDto(doctorRepository.save(doctor));
    }


    @Override
    @Transactional
    public void deleteDoctor(Long id) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Doctor no encontrado con el ID: " + id));

        if (doctor.getSpecialities() != null && !doctor.getSpecialities().isEmpty()) {
            for (Speciality speciality : doctor.getSpecialities()) {
                speciality.getDoctors().remove(doctor);
            }
            doctor.getSpecialities().clear();
        }

        if (doctor.getUser() != null) {
            userService.deleteUser(doctor.getUser().getId());
        }
        doctorRepository.delete(doctor);
    }
}
