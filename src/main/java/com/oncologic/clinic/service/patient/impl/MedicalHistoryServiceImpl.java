package com.oncologic.clinic.service.patient.impl;

import com.oncologic.clinic.dto.patient.request.MedicalHistoryRequestDTO;
import com.oncologic.clinic.dto.patient.response.MedicalHistoryResponseDTO;
import com.oncologic.clinic.dto.patient.update.MedicalHistoryUpdateDTO;
import com.oncologic.clinic.entity.patient.MedicalHistory;
import com.oncologic.clinic.entity.patient.Patient;
import com.oncologic.clinic.exception.runtime.patient.DuplicateMedicalHistoryException;
import com.oncologic.clinic.exception.runtime.patient.MedicalHistoryNotFoundException;
import com.oncologic.clinic.exception.runtime.patient.PatientNotFoundException;
import com.oncologic.clinic.mapper.patient.MedicalHistoryMapper;
import com.oncologic.clinic.repository.patient.MedicalHistoryRepository;
import com.oncologic.clinic.repository.patient.PatientRepository;
import com.oncologic.clinic.service.patient.MedicalHistoryService;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MedicalHistoryServiceImpl implements MedicalHistoryService {
    private static final Logger logger = LoggerFactory.getLogger(MedicalHistoryServiceImpl.class);

    private final MedicalHistoryRepository medicalHistoryRepository;
    private final MedicalHistoryMapper medicalHistoryMapper;
    private final PatientRepository patientRepository;

    public MedicalHistoryServiceImpl(MedicalHistoryRepository medicalHistoryRepository,
                                     MedicalHistoryMapper medicalHistoryMapper,
                                     PatientRepository patientRepository) {
        this.medicalHistoryRepository = medicalHistoryRepository;
        this.medicalHistoryMapper = medicalHistoryMapper;
        this.patientRepository = patientRepository;
    }

    @Override
    public MedicalHistoryResponseDTO getMedicalHistoryById(Long id) {
        logger.info("Fetching medical history with ID: {}", id);
        MedicalHistory history = medicalHistoryRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Medical history not found with ID: {}", id);
                    return new MedicalHistoryNotFoundException(id);
                });
        return medicalHistoryMapper.toDto(history);
    }

    @Override
    public List<MedicalHistoryResponseDTO> getAllMedicalHistories() {
        logger.info("Retrieving all medical histories");
        List<MedicalHistoryResponseDTO> histories = medicalHistoryRepository.findAll().stream()
                .map(medicalHistoryMapper::toDto)
                .toList();

        if (histories.isEmpty()) {
            logger.info("No registered medical histories found");
        }
        return histories;
    }

    @Override
    @Transactional
    public MedicalHistory registerMedicalHistory(Patient patient, String currentHealthStatus) {
        logger.info("Registering new medical history for patient ID: {}", patient.getId());

        if (medicalHistoryRepository.existsByPatientId(patient.getId())) {
            logger.error("The patient with ID {} already has a medical history", patient.getId());
            throw new DuplicateMedicalHistoryException(patient.getId());
        }

        MedicalHistory medicalHistory = new MedicalHistory();
        medicalHistory.setPatient(patient);
        medicalHistory.setCreationDate(LocalDateTime.now());
        medicalHistory.setCurrentHealthStatus(
                currentHealthStatus != null ? currentHealthStatus : "No information");

        try {
            MedicalHistory saved = medicalHistoryRepository.save(medicalHistory);
            logger.info("Medical history successfully created with ID: {}", saved.getId());
            return saved;
        } catch (DataIntegrityViolationException e) {
            logger.error("Integrity error while creating medical history: {}", e.getMessage());
            throw new DataIntegrityViolationException("Error saving the medical history", e);
        }
    }

    @Override
    @Transactional
    public MedicalHistoryResponseDTO createMedicalHistory(MedicalHistoryRequestDTO dto) {
        logger.info("Creating new medical history for patient ID: {}", dto.getPatientId());

        Patient patient = patientRepository.findById(dto.getPatientId())
                .orElseThrow(() -> {
                    logger.warn("Patient not found with ID: {}", dto.getPatientId());
                    return new PatientNotFoundException(dto.getPatientId());
                });

        if (medicalHistoryRepository.existsByPatientId(patient.getId())) {
            logger.error("The patient with ID {} already has a medical history", patient.getId());
            throw new DuplicateMedicalHistoryException(patient.getId());
        }

        MedicalHistory history = medicalHistoryMapper.toEntity(dto);
        history.setPatient(patient);

        try {
            MedicalHistory saved = medicalHistoryRepository.save(history);
            logger.info("Medical history successfully created with ID: {}", saved.getId());
            return medicalHistoryMapper.toDto(saved);
        } catch (DataIntegrityViolationException e) {
            logger.error("Integrity error while creating medical history: {}", e.getMessage());
            throw new DataIntegrityViolationException("Error saving the medical history", e);
        }
    }

    @Override
    @Transactional
    public MedicalHistoryResponseDTO updateMedicalHistory(Long id, MedicalHistoryUpdateDTO dto) {
        logger.info("Updating medical history with ID: {}", id);

        MedicalHistory medicalHistory = medicalHistoryRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Medical history not found with ID: {}", id);
                    return new MedicalHistoryNotFoundException(id);
                });

        medicalHistoryMapper.updateEntityFromDto(dto, medicalHistory);

        try {
            MedicalHistory updated = medicalHistoryRepository.save(medicalHistory);
            logger.info("Medical history successfully updated with ID: {}", id);
            return medicalHistoryMapper.toDto(updated);
        } catch (DataIntegrityViolationException e) {
            logger.error("Integrity error while updating medical history: {}", e.getMessage());
            throw new DataIntegrityViolationException("Error updating the medical history", e);
        }
    }

    @Override
    @Transactional
    public void deleteMedicalHistory(Long id) {
        logger.info("Deleting medical history with ID: {}", id);

        MedicalHistory history = medicalHistoryRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Medical history not found with ID: {}", id);
                    return new MedicalHistoryNotFoundException(id);
                });

        try {
            medicalHistoryRepository.delete(history);
            logger.info("Medical history successfully deleted with ID: {}", id);
        } catch (DataIntegrityViolationException e) {
            logger.error("Error deleting medical history: {}", e.getMessage());
            throw new DataIntegrityViolationException(
                    "Cannot delete the medical history because it has associated records", e);
        }
    }
}