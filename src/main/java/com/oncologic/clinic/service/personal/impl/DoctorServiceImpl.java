package com.oncologic.clinic.service.personal.impl;

import com.oncologic.clinic.dto.SpecialityDTO;
import com.oncologic.clinic.dto.registration.RegisterDoctorDTO;
import com.oncologic.clinic.entity.personal.Doctor;
import com.oncologic.clinic.entity.personal.Speciality;
import com.oncologic.clinic.entity.user.User;
import com.oncologic.clinic.repository.personal.DoctorRepository;
import com.oncologic.clinic.repository.personal.SpecialityRepository;
import com.oncologic.clinic.service.personal.SpecialityService;
import com.oncologic.clinic.service.personal.DoctorService;
import com.oncologic.clinic.service.user.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class DoctorServiceImpl implements DoctorService {
    private final DoctorRepository doctorRepository;
    private final UserService userService;
    private final SpecialityService specialityService;
    private final SpecialityRepository specialityRepository;

    DoctorServiceImpl(DoctorRepository doctorRepository, UserService userService, SpecialityService specialityService, SpecialityRepository specialityRepository) {
        this.doctorRepository = doctorRepository;
        this.userService = userService;
        this.specialityService = specialityService;
        this.specialityRepository = specialityRepository;
    }

    @Override
    @Transactional
    public Doctor registerDoctor(RegisterDoctorDTO doctorDTO) {
        User user = userService.registerUser(doctorDTO);

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
        if (doctorDTO.getSelectedSpecialityId() != null) {
            specialityService.getSpecialityById(doctorDTO.getSelectedSpecialityId()).ifPresent(s -> {
                s.setDoctor(savedDoctor);
                specialityRepository.save(s);
            });
        } else if (doctorDTO.getNewSpecialityName() != null && !doctorDTO.getNewSpecialityName().isBlank()) {
            SpecialityDTO newSpecialityDTO = new SpecialityDTO();
            newSpecialityDTO.setName(doctorDTO.getNewSpecialityName());
            newSpecialityDTO.setDescription(doctorDTO.getNewSpecialityDescription());
            specialityService.registerSpeciality(newSpecialityDTO, savedDoctor);
        }

        return savedDoctor;
    }
}
