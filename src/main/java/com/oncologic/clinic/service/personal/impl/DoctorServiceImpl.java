package com.oncologic.clinic.service.personal.impl;

import com.oncologic.clinic.dto.register.RegisterDoctorDTO;
import com.oncologic.clinic.entity.personal.Doctor;
import com.oncologic.clinic.entity.user.User;
import com.oncologic.clinic.repository.personal.DoctorRepository;
import com.oncologic.clinic.service.personal.DoctorService;
import com.oncologic.clinic.service.user.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class DoctorServiceImpl implements DoctorService {
    private final DoctorRepository doctorRepository;
    private final UserService userService;

    DoctorServiceImpl(DoctorRepository doctorRepository, UserService userService) {
        this.doctorRepository = doctorRepository;
        this.userService = userService;
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

        return doctorRepository.save(doctor);
    }
}
