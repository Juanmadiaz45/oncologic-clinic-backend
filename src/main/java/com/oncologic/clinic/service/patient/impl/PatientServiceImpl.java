package com.oncologic.clinic.service.patient.impl;

import com.oncologic.clinic.dto.patient.request.MedicalHistoryRequestDTO;
import com.oncologic.clinic.dto.patient.request.PatientRequestDTO;
import com.oncologic.clinic.dto.patient.response.PatientResponseDTO;
import com.oncologic.clinic.dto.patient.update.PatientUpdateDTO;
import com.oncologic.clinic.dto.registration.RegisterPatientDTO;
import com.oncologic.clinic.dto.user.UserDTO;
import com.oncologic.clinic.dto.user.response.UserResponseDTO;
import com.oncologic.clinic.entity.patient.MedicalHistory;
import com.oncologic.clinic.entity.patient.Patient;
import com.oncologic.clinic.entity.user.User;
import com.oncologic.clinic.mapper.patient.MedicalHistoryMapper;
import com.oncologic.clinic.mapper.patient.PatientMapper;
import com.oncologic.clinic.repository.patient.PatientRepository;
import com.oncologic.clinic.repository.user.UserRepository;
import com.oncologic.clinic.service.patient.MedicalHistoryService;
import com.oncologic.clinic.service.patient.PatientService;
import com.oncologic.clinic.service.user.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class PatientServiceImpl implements PatientService {
    private final PatientRepository patientRepository;
    private final UserService userService;
    private final MedicalHistoryService medicalHistoryService;
    private final PatientMapper patientMapper;
    private final MedicalHistoryMapper medicalHistoryMapper;
    private final UserRepository userRepository;

    PatientServiceImpl(PatientRepository patientRepository, UserService userService, MedicalHistoryService medicalHistoryService, PatientMapper patientMapper, MedicalHistoryMapper medicalHistoryMapper, UserRepository userRepository) {
        this.patientRepository = patientRepository;
        this.userService = userService;
        this.medicalHistoryService = medicalHistoryService;
        this.patientMapper = patientMapper;
        this.medicalHistoryMapper = medicalHistoryMapper;
        this.userRepository = userRepository;
    }

    @Override
    public PatientResponseDTO getPatientById(Long id) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Paciente con el ID " + id + " no encontrado"));

        return patientMapper.toDto(patient);
    }

    @Override
    public List<PatientResponseDTO> getAllPatients() {
        return patientRepository.findAll().stream().map(patientMapper::toDto).toList();
    }

    @Override
    @Transactional
    public Patient registerPatient(RegisterPatientDTO patientDTO){
        UserResponseDTO userResponseDTO = userService.createUser(patientDTO.getUserData());
        User user = userRepository.findById(userResponseDTO.getId())
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con ID: " + userResponseDTO.getId()));


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

    @Override
    @Transactional
    public PatientResponseDTO createPatient(PatientRequestDTO patientDTO){
        UserDTO userRequestDTO = patientDTO.getUserData();
        UserResponseDTO userResponse = userService.createUser(userRequestDTO);
        User user = userService.getUserEntityById(userResponse.getId());

        Patient patient = patientMapper.toEntity(patientDTO);
        patient.setUser(user);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate date = LocalDate.parse(patientDTO.getBirthDate(), formatter);
        patient.setBirthdate(date.atStartOfDay());

        Patient savedPatient = patientRepository.save(patient);

        MedicalHistoryRequestDTO historyDTO = new MedicalHistoryRequestDTO();
        historyDTO.setPatientId(savedPatient.getId());
        historyDTO.setCurrentHealthStatus(patientDTO.getCurrentHealthStatus());

        medicalHistoryService.createMedicalHistory(historyDTO);

        MedicalHistory medicalHistory = medicalHistoryMapper.toEntity(historyDTO);
        savedPatient.setMedicalHistory(medicalHistory);
        Patient updatedPatient = patientRepository.save(savedPatient);

        return patientMapper.toDto(updatedPatient);
    }

    @Override
    @Transactional
    public PatientResponseDTO updatePatient(Long id, PatientUpdateDTO patientDTO) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Paciente con el ID " + id + " no encontrado"));

        patientMapper.updateEntityFromDto(patientDTO, patient);

        return patientMapper.toDto(patientRepository.save(patient));
    }

    public void deletePatient(Long id){
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Paciente no encontrado con el ID: " + id));
        if (patient.getUser() != null) {
            userService.deleteUser(patient.getUser().getId());
        }
        patientRepository.deleteById(id);
    }
}
