package com.oncologic.clinic.service.examination.impl;

import com.oncologic.clinic.dto.examination.request.MedicalExaminationRequestDTO;
import com.oncologic.clinic.dto.examination.response.MedicalExaminationResponseDTO;
import com.oncologic.clinic.dto.examination.update.MedicalExaminationUpdateDTO;
import com.oncologic.clinic.entity.examination.Laboratory;
import com.oncologic.clinic.entity.examination.MedicalExamination;
import com.oncologic.clinic.entity.examination.TypeOfExam;
import com.oncologic.clinic.entity.patient.MedicalHistory;
import com.oncologic.clinic.entity.patient.Patient;
import com.oncologic.clinic.mapper.examination.MedicalExaminationMapper;
import com.oncologic.clinic.repository.examination.LaboratoryRepository;
import com.oncologic.clinic.repository.examination.MedicalExaminationRepository;
import com.oncologic.clinic.repository.examination.TypeOfExamRepository;
import com.oncologic.clinic.repository.patient.MedicalHistoryRepository;
import com.oncologic.clinic.repository.patient.PatientRepository;
import com.oncologic.clinic.service.examination.MedicalExaminationManagementService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MedicalExaminationManagementServiceImpl implements MedicalExaminationManagementService {

    private final PatientRepository patientRepository;
    private final MedicalHistoryRepository medicalHistoryRepository;
    private final MedicalExaminationRepository medicalExaminationRepository;
    private final LaboratoryRepository laboratoryRepository;
    private final TypeOfExamRepository typeOfExamRepository;
    private final MedicalExaminationMapper medicalExaminationMapper;

    @Override
    @Transactional
    public MedicalExaminationResponseDTO createExaminationForPatient(Long patientId, MedicalExaminationRequestDTO requestDTO) {
        // Find patient
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new EntityNotFoundException("Patient not found with ID: " + patientId));

        // Get medical history
        MedicalHistory medicalHistory = patient.getMedicalHistory();
        if (medicalHistory == null) {
            throw new EntityNotFoundException("Medical history not found for patient: " + patientId);
        }

        // Find laboratory
        Laboratory laboratory = laboratoryRepository.findById(requestDTO.getLaboratoryId())
                .orElseThrow(() -> new EntityNotFoundException("Laboratory not found with ID: " + requestDTO.getLaboratoryId()));

        // Find type of exam
        TypeOfExam typeOfExam = typeOfExamRepository.findById(requestDTO.getTypeOfExamId())
                .orElseThrow(() -> new EntityNotFoundException("Type of exam not found with ID: " + requestDTO.getTypeOfExamId()));

        // Create examination entity
        MedicalExamination examination = new MedicalExamination();
        examination.setId(requestDTO.getId());
        examination.setDateOfRealization(requestDTO.getDateOfRealization());
        examination.setLaboratory(laboratory);
        examination.setTypeOfExam(typeOfExam);
        examination.setMedicalHistory(medicalHistory);

        // Save examination
        examination = medicalExaminationRepository.save(examination);

        return medicalExaminationMapper.toDto(examination);
    }

    @Override
    public List<MedicalExaminationResponseDTO> getExaminationsByPatientId(Long patientId) {
        // Find patient
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new EntityNotFoundException("Patient not found with ID: " + patientId));

        // Get medical history
        MedicalHistory medicalHistory = patient.getMedicalHistory();
        if (medicalHistory == null) {
            throw new EntityNotFoundException("Medical history not found for patient: " + patientId);
        }

        // Get examinations by medical history
        List<MedicalExamination> examinations = medicalExaminationRepository.findByMedicalHistoryId(medicalHistory.getId());

        return examinations.stream()
                .map(medicalExaminationMapper::toDto)
                .toList();
    }

    @Override
    public Map<String, Object> getExaminationDetailsWithResults(String examinationId) {
        MedicalExamination examination = medicalExaminationRepository.findById(examinationId)
                .orElseThrow(() -> new EntityNotFoundException("Medical examination not found with ID: " + examinationId));

        Map<String, Object> result = new HashMap<>();
        result.put("examination", medicalExaminationMapper.toDto(examination));
        // Add results when implemented
        return result;
    }

    @Override
    @Transactional
    public MedicalExaminationResponseDTO updateExamination(String examinationId, MedicalExaminationUpdateDTO updateDTO) {
        MedicalExamination examination = medicalExaminationRepository.findById(examinationId)
                .orElseThrow(() -> new EntityNotFoundException("Medical examination not found with ID: " + examinationId));

        // Update laboratory if provided
        if (updateDTO.getLaboratoryId() != null) {
            Laboratory laboratory = laboratoryRepository.findById(updateDTO.getLaboratoryId())
                    .orElseThrow(() -> new EntityNotFoundException("Laboratory not found with ID: " + updateDTO.getLaboratoryId()));
            examination.setLaboratory(laboratory);
        }

        // Update type of exam if provided
        if (updateDTO.getTypeOfExamId() != null) {
            TypeOfExam typeOfExam = typeOfExamRepository.findById(updateDTO.getTypeOfExamId())
                    .orElseThrow(() -> new EntityNotFoundException("Type of exam not found with ID: " + updateDTO.getTypeOfExamId()));
            examination.setTypeOfExam(typeOfExam);
        }

        // Update other fields
        medicalExaminationMapper.updateEntityFromDto(updateDTO, examination);
        examination = medicalExaminationRepository.save(examination);

        return medicalExaminationMapper.toDto(examination);
    }

    @Override
    @Transactional
    public void deleteExamination(String examinationId) {
        if (!medicalExaminationRepository.existsById(examinationId)) {
            throw new EntityNotFoundException("Medical examination not found with ID: " + examinationId);
        }
        medicalExaminationRepository.deleteById(examinationId);
    }

    @Override
    public Map<String, Object> getCompleteMedicalHistoryWithExaminations(Long patientId) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new EntityNotFoundException("Patient not found with ID: " + patientId));

        MedicalHistory medicalHistory = patient.getMedicalHistory();
        if (medicalHistory == null) {
            throw new EntityNotFoundException("Medical history not found for patient: " + patientId);
        }

        List<MedicalExamination> examinations = medicalExaminationRepository.findByMedicalHistoryId(medicalHistory.getId());

        Map<String, Object> result = new HashMap<>();
        result.put("patient", patient);
        result.put("medicalHistory", medicalHistory);
        result.put("examinations", examinations.stream().map(medicalExaminationMapper::toDto).toList());

        return result;
    }
}