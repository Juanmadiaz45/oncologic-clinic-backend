package com.oncologic.clinic.service.patient.impl;

import com.oncologic.clinic.dto.registration.RegisterPatientDTO;
import com.oncologic.clinic.entity.patient.MedicalHistory;
import com.oncologic.clinic.entity.patient.Patient;
import com.oncologic.clinic.entity.user.User;
import com.oncologic.clinic.repository.patient.PatientRepository;
import com.oncologic.clinic.service.patient.MedicalHistoryService;
import com.oncologic.clinic.service.patient.PatientService;
import com.oncologic.clinic.service.user.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
public class PatientServiceImpl implements PatientService {
    private final PatientRepository patientRepository;
    private final UserService userService;
    private final MedicalHistoryService medicalHistoryService;

    PatientServiceImpl(PatientRepository patientRepository, UserService userService, MedicalHistoryService medicalHistoryService) {
        this.patientRepository = patientRepository;
        this.userService = userService;
        this.medicalHistoryService = medicalHistoryService;
    }

    @Override
    @Transactional
    public Patient registerPatient(RegisterPatientDTO patientDTO){
        User user = userService.createUser(patientDTO);

        Patient patient = new Patient();
        patient.setUser(user);
        patient.setName(patientDTO.getName());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate date = LocalDate.parse(patientDTO.getBirthDate(), formatter);
        patient.setBirthdate(date.atStartOfDay());
        patient.setGender(patientDTO.getGender());
        patient.setAddress(patientDTO.getAddress());
        patient.setPhoneNumber(String.valueOf(patientDTO.getPhoneNumber()));
        patient.setEmail(patientDTO.getEmail());

        Patient savedPatient = patientRepository.save(patient);

        MedicalHistory medicalHistory = medicalHistoryService.registerMedicalHistory(savedPatient, patientDTO.getCurrentHealthStatus());

        savedPatient.setMedicalHistory(medicalHistory);

        return patientRepository.save(savedPatient);
    }
}
