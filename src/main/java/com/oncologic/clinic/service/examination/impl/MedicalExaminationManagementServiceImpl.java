package com.oncologic.clinic.service.examination.impl;

import com.oncologic.clinic.dto.examination.request.MedicalExaminationRequestDTO;
import com.oncologic.clinic.dto.examination.response.ExaminationResultResponseDTO;
import com.oncologic.clinic.dto.examination.response.MedicalExaminationResponseDTO;
import com.oncologic.clinic.dto.examination.update.MedicalExaminationUpdateDTO;
import com.oncologic.clinic.dto.patient.response.MedicalHistoryResponseDTO;
import com.oncologic.clinic.entity.examination.Laboratory;
import com.oncologic.clinic.entity.examination.MedicalExamination;
import com.oncologic.clinic.entity.examination.TypeOfExam;
import com.oncologic.clinic.entity.patient.MedicalHistory;
import com.oncologic.clinic.entity.patient.Patient;
import com.oncologic.clinic.exception.runtime.examination.LaboratoryNotFoundException;
import com.oncologic.clinic.exception.runtime.examination.MedicalExaminationNotFoundException;
import com.oncologic.clinic.exception.runtime.examination.TypeOfExamNotFoundException;
import com.oncologic.clinic.exception.runtime.patient.MedicalHistoryNotFoundException;
import com.oncologic.clinic.exception.runtime.patient.PatientNotFoundException;
import com.oncologic.clinic.mapper.examination.MedicalExaminationMapper;
import com.oncologic.clinic.mapper.patient.MedicalHistoryMapper;
import com.oncologic.clinic.repository.examination.LaboratoryRepository;
import com.oncologic.clinic.repository.examination.MedicalExaminationRepository;
import com.oncologic.clinic.repository.examination.TypeOfExamRepository;
import com.oncologic.clinic.repository.patient.MedicalHistoryRepository;
import com.oncologic.clinic.repository.patient.PatientRepository;
import com.oncologic.clinic.service.examination.ExaminationResultService;
import com.oncologic.clinic.service.examination.MedicalExaminationManagementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class MedicalExaminationManagementServiceImpl implements MedicalExaminationManagementService {
    private final PatientRepository patientRepository;
    private final MedicalHistoryRepository medicalHistoryRepository;
    private final MedicalExaminationRepository medicalExaminationRepository;
    private final LaboratoryRepository laboratoryRepository;
    private final TypeOfExamRepository typeOfExamRepository;
    private final ExaminationResultService examinationResultService;
    private final MedicalExaminationMapper medicalExaminationMapper;
    private final MedicalHistoryMapper medicalHistoryMapper;

    @Override
    @Transactional
    public MedicalExaminationResponseDTO createExaminationForPatient(Long patientId, MedicalExaminationRequestDTO requestDTO) {
        log.info("Creating medical examination for patient ID: {}", patientId);

        // Get patient and validate
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new PatientNotFoundException(patientId));

        // Get medical history
        MedicalHistory medicalHistory = patient.getMedicalHistory();
        if (medicalHistory == null) {
            throw new MedicalHistoryNotFoundException(patientId);
        }

        // Validate laboratory
        Laboratory laboratory = laboratoryRepository.findById(requestDTO.getLaboratoryId())
                .orElseThrow(() -> new LaboratoryNotFoundException(requestDTO.getLaboratoryId()));

        // Validate type of exam
        TypeOfExam typeOfExam = typeOfExamRepository.findById(requestDTO.getTypeOfExamId())
                .orElseThrow(() -> new TypeOfExamNotFoundException(requestDTO.getTypeOfExamId()));

        // Create examination
        MedicalExamination examination = medicalExaminationMapper.toEntity(requestDTO);
        examination.setLaboratory(laboratory);
        examination.setTypeOfExam(typeOfExam);
        examination.setMedicalHistory(medicalHistory);

        MedicalExamination savedExamination = medicalExaminationRepository.save(examination);
        log.info("Medical examination created with ID: {} for patient ID: {}", savedExamination.getId(), patientId);

        return medicalExaminationMapper.toDto(savedExamination);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MedicalExaminationResponseDTO> getExaminationsByPatientId(Long patientId) {
        log.info("Getting examinations for patient ID: {}", patientId);

        // Validate patient exists
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new PatientNotFoundException(patientId));

        // Get medical history
        MedicalHistory medicalHistory = patient.getMedicalHistory();
        if (medicalHistory == null) {
            throw new MedicalHistoryNotFoundException(patientId);
        }

        // Get examinations
        List<MedicalExamination> examinations = medicalExaminationRepository.findByMedicalHistoryId(medicalHistory.getId());

        return examinations.stream()
                .map(medicalExaminationMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getExaminationDetailsWithResults(String examinationId) {
        log.info("Getting examination details with results for examination ID: {}", examinationId);

        // Get examination
        MedicalExamination examination = medicalExaminationRepository.findById(examinationId)
                .orElseThrow(() -> new MedicalExaminationNotFoundException(examinationId));

        // Get examination results
        List<ExaminationResultResponseDTO> results = examinationResultService
                .getExaminationResultsByMedicalHistoryId(examination.getMedicalHistory().getId());

        // Build response
        Map<String, Object> response = new HashMap<>();
        response.put("examination", medicalExaminationMapper.toDto(examination));
        response.put("results", results);
        response.put("patientName", examination.getMedicalHistory().getPatient().getName());
        response.put("patientId", examination.getMedicalHistory().getPatient().getId());
        response.put("medicalHistoryId", examination.getMedicalHistory().getId());

        return response;
    }

    @Override
    @Transactional
    public MedicalExaminationResponseDTO updateExamination(String examinationId, MedicalExaminationUpdateDTO updateDTO) {
        log.info("Updating examination ID: {}", examinationId);

        // Get existing examination
        MedicalExamination examination = medicalExaminationRepository.findById(examinationId)
                .orElseThrow(() -> new MedicalExaminationNotFoundException(examinationId));

        // Validate laboratory if provided
        if (updateDTO.getLaboratoryId() != null) {
            Laboratory laboratory = laboratoryRepository.findById(updateDTO.getLaboratoryId())
                    .orElseThrow(() -> new LaboratoryNotFoundException(updateDTO.getLaboratoryId()));
            examination.setLaboratory(laboratory);
        }

        // Validate type of exam if provided
        if (updateDTO.getTypeOfExamId() != null) {
            TypeOfExam typeOfExam = typeOfExamRepository.findById(updateDTO.getTypeOfExamId())
                    .orElseThrow(() -> new TypeOfExamNotFoundException(updateDTO.getTypeOfExamId()));
            examination.setTypeOfExam(typeOfExam);
        }

        // Update examination
        medicalExaminationMapper.updateEntityFromDto(updateDTO, examination);
        MedicalExamination updatedExamination = medicalExaminationRepository.save(examination);

        log.info("Examination updated with ID: {}", examinationId);
        return medicalExaminationMapper.toDto(updatedExamination);
    }

    @Override
    @Transactional
    public void deleteExamination(String examinationId) {
        log.info("Deleting examination ID: {}", examinationId);

        if (!medicalExaminationRepository.existsById(examinationId)) {
            throw new MedicalExaminationNotFoundException(examinationId);
        }

        medicalExaminationRepository.deleteById(examinationId);
        log.info("Examination deleted with ID: {}", examinationId);
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getCompleteMedicalHistoryWithExaminations(Long patientId) {
        log.info("Getting complete medical history with examinations for patient ID: {}", patientId);

        // Validate patient exists
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new PatientNotFoundException(patientId));

        // Get medical history
        MedicalHistory medicalHistory = patient.getMedicalHistory();
        if (medicalHistory == null) {
            throw new MedicalHistoryNotFoundException(patientId);
        }

        // Get medical history details
        MedicalHistoryResponseDTO medicalHistoryDTO = medicalHistoryMapper.toDto(medicalHistory);

        // Get examinations
        List<MedicalExaminationResponseDTO> examinations = medicalExaminationRepository
                .findByMedicalHistoryId(medicalHistory.getId())
                .stream()
                .map(medicalExaminationMapper::toDto)
                .collect(Collectors.toList());

        // Get examination results
        List<ExaminationResultResponseDTO> results = examinationResultService
                .getExaminationResultsByMedicalHistoryId(medicalHistory.getId());

        // Build complete response
        Map<String, Object> response = new HashMap<>();
        response.put("patient", Map.of(
                "id", patient.getId(),
                "name", patient.getName(),
                "idNumber", patient.getIdNumber(),
                "email", patient.getEmail(),
                "phoneNumber", patient.getPhoneNumber()
        ));
        response.put("medicalHistory", medicalHistoryDTO);
        response.put("examinations", examinations);
        response.put("examinationResults", results);
        response.put("totalExaminations", examinations.size());
        response.put("totalResults", results.size());

        return response;
    }

}
