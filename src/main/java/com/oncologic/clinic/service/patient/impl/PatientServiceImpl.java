package com.oncologic.clinic.service.patient.impl;

import com.oncologic.clinic.dto.patient.request.MedicalHistoryRequestDTO;
import com.oncologic.clinic.dto.patient.request.PatientRequestDTO;
import com.oncologic.clinic.dto.patient.response.PatientResponseDTO;
import com.oncologic.clinic.dto.patient.update.PatientUpdateDTO;
import com.oncologic.clinic.dto.registration.RegisterPatientDTO;
import com.oncologic.clinic.dto.user.response.UserResponseDTO;
import com.oncologic.clinic.entity.patient.MedicalHistory;
import com.oncologic.clinic.entity.patient.Patient;
import com.oncologic.clinic.entity.user.User;
import com.oncologic.clinic.exception.runtime.patient.PatientNotFoundException;
import com.oncologic.clinic.exception.runtime.patient.UserCreationException;
import com.oncologic.clinic.mapper.patient.PatientMapper;
import com.oncologic.clinic.repository.patient.PatientRepository;
import com.oncologic.clinic.repository.user.UserRepository;
import com.oncologic.clinic.service.patient.MedicalHistoryService;
import com.oncologic.clinic.service.patient.PatientService;
import com.oncologic.clinic.service.user.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PatientServiceImpl implements PatientService {
    private static final Logger logger = LoggerFactory.getLogger(PatientServiceImpl.class);

    private final PatientRepository patientRepository;
    private final UserService userService;
    private final MedicalHistoryService medicalHistoryService;
    private final PatientMapper patientMapper;
    private final UserRepository userRepository;

    public PatientServiceImpl(PatientRepository patientRepository,
                              UserService userService,
                              MedicalHistoryService medicalHistoryService,
                              PatientMapper patientMapper,
                              UserRepository userRepository) {
        this.patientRepository = patientRepository;
        this.userService = userService;
        this.medicalHistoryService = medicalHistoryService;
        this.patientMapper = patientMapper;
        this.userRepository = userRepository;
    }

    @Override
    public PatientResponseDTO getPatientById(Long id) {
        logger.info("Fetching patient with ID: {}", id);
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Patient not found with ID: {}", id);
                    return new PatientNotFoundException(id);
                });
        return patientMapper.toDto(patient);
    }

    @Override
    public List<PatientResponseDTO> getAllPatients() {
        logger.info("Fetching all patients");
        List<PatientResponseDTO> patients = patientRepository.findAll()
                .stream()
                .map(patientMapper::toDto)
                .toList();

        if (patients.isEmpty()) {
            logger.info("No patients found in database");
        }
        return patients;
    }

    @Override
    @Transactional
    public Patient registerPatient(RegisterPatientDTO patientDTO) {
        UserResponseDTO userResponseDTO = userService.createUser(patientDTO.getUserData());
        User user = userRepository.findById(userResponseDTO.getId())
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con ID: " + userResponseDTO.getId()));

        Patient patient = new Patient();
        patient.setUser(user);
        patient.setIdNumber(patientDTO.getIdNumber());
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

    @Override
    @Transactional
    public PatientResponseDTO createPatient(PatientRequestDTO patientDTO) {
        logger.info("Creating new patient with username: {}", patientDTO.getUserData().getUsername());

        try {
            UserResponseDTO userResponse;
            try {
                userResponse = userService.createUser(patientDTO.getUserData());
            } catch (Exception e) {
                logger.error("Failed to create user: {}", e.getMessage());
                throw new UserCreationException("Failed to create user: " + e.getMessage());
            }

            User user = userService.getUserEntityById(userResponse.getId());

            Patient patient = patientMapper.toEntity(patientDTO);
            patient.setUser(user);

            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                LocalDate date = LocalDate.parse(patientDTO.getBirthDate(), formatter);
                patient.setBirthdate(date.atStartOfDay());
            } catch (DateTimeParseException e) {
                logger.error("Invalid date format for birthDate: {}", patientDTO.getBirthDate());
                throw new IllegalArgumentException("Invalid date format. Expected yyyy-MM-dd");
            }

            Patient savedPatient = patientRepository.save(patient);
            logger.info("Patient created with ID: {}", savedPatient.getId());

            MedicalHistoryRequestDTO historyDTO = new MedicalHistoryRequestDTO();
            historyDTO.setPatientId(savedPatient.getId());
            historyDTO.setCurrentHealthStatus(patientDTO.getCurrentHealthStatus());

            medicalHistoryService.createMedicalHistory(historyDTO);

            Patient updatedPatient = patientRepository.findById(savedPatient.getId())
                    .orElseThrow(() -> new PatientNotFoundException(savedPatient.getId()));

            return patientMapper.toDto(updatedPatient);

        } catch (DataIntegrityViolationException e) {
            logger.error("Data integrity violation while creating patient: {}", e.getMessage());
            throw new DataIntegrityViolationException("Failed to create patient due to data integrity violation", e);
        }
    }

    @Override
    @Transactional
    public PatientResponseDTO updatePatient(Long id, PatientUpdateDTO patientDTO) {
        logger.info("Updating patient with ID: {}", id);

        try {
            Patient patient = patientRepository.findById(id)
                    .orElseThrow(() -> {
                        logger.warn("Patient not found with ID: {}", id);
                        return new PatientNotFoundException(id);
                    });

            patientMapper.updateEntityFromDto(patientDTO, patient);

            if (patientDTO.getBirthDate() != null) {
                try {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    LocalDate date = LocalDate.parse(patientDTO.getBirthDate(), formatter);
                    patient.setBirthdate(date.atStartOfDay());
                } catch (DateTimeParseException e) {
                    logger.error("Invalid date format for birthDate: {}", patientDTO.getBirthDate());
                    throw new IllegalArgumentException("Invalid date format. Expected yyyy-MM-dd");
                }
            }

            Patient updated = patientRepository.save(patient);
            logger.info("Patient updated successfully with ID: {}", id);
            return patientMapper.toDto(updated);

        } catch (DataIntegrityViolationException e) {
            logger.error("Data integrity violation while updating patient: {}", e.getMessage());
            throw new DataIntegrityViolationException("Failed to update patient due to data integrity violation", e);
        }
    }

    @Override
    @Transactional
    public void deletePatient(Long id) {
        logger.info("Deleting patient with ID: {}", id);

        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Patient not found with ID: {}", id);
                    return new PatientNotFoundException(id);
                });

        patientRepository.delete(patient);
        logger.info("Patient record deleted with ID: {}", id);
    }

    @Override
    public List<PatientResponseDTO> searchPatientsByIdNumber(String idNumber) {
        if (idNumber == null || idNumber.trim().isEmpty()) {
            return new ArrayList<>();
        }

        List<Patient> patients = patientRepository.findByIdNumberContaining(idNumber.trim());
        return patients.stream()
                .map(patientMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Long getMedicalHistoryIdByPatientId(Long patientId) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new EntityNotFoundException("Patient not found with ID: " + patientId));

        if (patient.getMedicalHistory() == null) {
            throw new EntityNotFoundException("Medical history not found for patient with ID: " + patientId);
        }

        return patient.getMedicalHistory().getId();
    }
}
