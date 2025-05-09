package com.oncologic.clinic.service.patient.impl;

import com.oncologic.clinic.dto.patient.request.MedicalHistoryRequestDTO;
import com.oncologic.clinic.dto.patient.response.MedicalHistoryResponseDTO;
import com.oncologic.clinic.dto.patient.update.MedicalHistoryUpdateDTO;
import com.oncologic.clinic.entity.patient.MedicalHistory;
import com.oncologic.clinic.entity.patient.Patient;
import com.oncologic.clinic.mapper.patient.MedicalHistoryMapper;
import com.oncologic.clinic.repository.patient.MedicalHistoryRepository;
import com.oncologic.clinic.repository.patient.PatientRepository;
import com.oncologic.clinic.service.patient.MedicalHistoryService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MedicalHistoryServiceImpl implements MedicalHistoryService {
    private final MedicalHistoryRepository medicalHistoryRepository;
    private final MedicalHistoryMapper medicalHistoryMapper;
    private final PatientRepository patientRepository;

    public MedicalHistoryServiceImpl(MedicalHistoryRepository medicalHistoryRepository, MedicalHistoryMapper medicalHistoryMapper, PatientRepository patientRepository) {
        this.medicalHistoryRepository = medicalHistoryRepository;
        this.medicalHistoryMapper = medicalHistoryMapper;
        this.patientRepository = patientRepository;
    }

    @Override
    public MedicalHistoryResponseDTO getMedicalHistoryById(Long id) {
        MedicalHistory history = medicalHistoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Historia médica no encontrada con el ID: " + id));
        return medicalHistoryMapper.toDto(history);
    }

    @Override
    public List<MedicalHistoryResponseDTO> getAllMedicalHistories() {
        return medicalHistoryRepository.findAll().stream()
                .map(medicalHistoryMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public MedicalHistoryResponseDTO createMedicalHistory(MedicalHistoryRequestDTO dto) {
        Patient patient = patientRepository.findById(dto.getPatientId())
                .orElseThrow(() -> new EntityNotFoundException("Paciente no encontrado con el ID: " + dto.getPatientId()));

        MedicalHistory history = medicalHistoryMapper.toEntity(dto);
        history.setPatient(patient);

        MedicalHistory saved = medicalHistoryRepository.save(history);
        return medicalHistoryMapper.toDto(saved);
    }

    @Override
    @Transactional
    public MedicalHistoryResponseDTO updateMedicalHistory(Long id, MedicalHistoryUpdateDTO medicalHistoryDTO) {
        MedicalHistory medicalHistory = medicalHistoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Historia médica con el ID " + id + " no encontrada"));

        medicalHistoryMapper.updateEntityFromDto(medicalHistoryDTO, medicalHistory);

        return medicalHistoryMapper.toDto(medicalHistoryRepository.save(medicalHistory));
    }

    @Override
    @Transactional
    public void deleteMedicalHistory(Long id) {
        MedicalHistory history = medicalHistoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Historia médica no encontrada con el ID: " + id));
        medicalHistoryRepository.delete(history);
    }

}
