package com.oncologic.clinic.service.personal.impl;

import com.oncologic.clinic.dto.personal.request.DoctorRequestDTO;
import com.oncologic.clinic.dto.personal.response.DoctorResponseDTO;
import com.oncologic.clinic.dto.personal.update.DoctorUpdateDTO;
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
    public DoctorResponseDTO createDoctor(DoctorRequestDTO doctorDTO) {
        User user = userService.createUser(doctorDTO);
        Doctor doctor = doctorMapper.toEntity(doctorDTO);
        doctor.setUser(user);
        doctor.setDateOfHiring(LocalDateTime.now());
        doctor.setStatus('A');

        Doctor savedDoctor = doctorRepository.save(doctor);

        // Asociar especialidades existentes si hay IDs
        if (doctorDTO.getSpecialityIds() != null && !doctorDTO.getSpecialityIds().isEmpty()) {
            Set<Speciality> specialities = new HashSet<>();
            for (Long specialityId : doctorDTO.getSpecialityIds()) {
                Speciality speciality = specialityRepository.findById(specialityId)
                        .orElseThrow(() -> new EntityNotFoundException("Especialidad con el ID " + specialityId + " no encontrada"));

                speciality.getDoctors().add(savedDoctor);
                specialities.add(speciality);
            }
            specialityRepository.saveAll(specialities);
            savedDoctor.setSpecialities(specialities);
        }

        return doctorMapper.toDto(doctorRepository.save(savedDoctor));
    }


    @Override
    @Transactional
    public DoctorResponseDTO updateDoctor(Long id, DoctorUpdateDTO doctorUpdateDTO) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Doctor con el ID " + id + " no encontrado"));

        doctorMapper.updateEntityFromDto(doctorUpdateDTO, doctor);

        // Si vienen especialidades nuevas
        if (doctorUpdateDTO.getSpecialityIds() != null) {
            // 1. Quitar doctor de especialidades antiguas que ya no están
            Set<Speciality> currentSpecialities = doctor.getSpecialities() != null
                    ? new HashSet<>(doctor.getSpecialities())
                    : new HashSet<>();

            for (Speciality oldSpeciality : currentSpecialities) {
                if (!doctorUpdateDTO.getSpecialityIds().contains(oldSpeciality.getId())) {
                    oldSpeciality.getDoctors().remove(doctor);
                    specialityRepository.save(oldSpeciality);
                }
            }

            // 2. Agregar doctor a nuevas especialidades
            Set<Speciality> updatedSpecialities = new HashSet<>();
            for (Long specialityId : doctorUpdateDTO.getSpecialityIds()) {
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
