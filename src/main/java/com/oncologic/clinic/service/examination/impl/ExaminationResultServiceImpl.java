package com.oncologic.clinic.service.examination.impl;

import com.oncologic.clinic.dto.examination.request.ExaminationResultRequestDTO;
import com.oncologic.clinic.dto.examination.response.ExaminationResultResponseDTO;
import com.oncologic.clinic.dto.examination.update.ExaminationResultUpdateDTO;
import com.oncologic.clinic.entity.examination.ExaminationResult;
import com.oncologic.clinic.entity.examination.MedicalExamination;
import com.oncologic.clinic.entity.patient.MedicalHistory;
import com.oncologic.clinic.mapper.examination.ExaminationResultMapper;
import com.oncologic.clinic.repository.examination.ExaminationResultRepository;
import com.oncologic.clinic.repository.patient.MedicalHistoryRepository;
import com.oncologic.clinic.service.examination.ExaminationResultService;
import com.oncologic.clinic.service.examination.MedicalReportGenerator;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExaminationResultServiceImpl implements ExaminationResultService {

    private final ExaminationResultRepository examinationResultRepository;
    private final MedicalHistoryRepository medicalHistoryRepository;
    private final ExaminationResultMapper mapper;
    private final MedicalReportGenerator reportGenerator;

    @Override
    @Transactional(readOnly = true)
    public ExaminationResultResponseDTO getExaminationResultById(Long id) {
        ExaminationResult result = examinationResultRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Examination result not found with ID: " + id));

        ExaminationResultResponseDTO dto = mapper.toDto(result);

        // Agregar información del reporte
        if (result.getResultsReport() != null && !result.getResultsReport().trim().isEmpty()) {
            dto.setHasReport(true);
            dto.setReportSizeBytes(result.getResultsReport().length());
            dto.setReportFormat("text/plain");
        } else {
            dto.setHasReport(false);
            dto.setReportSizeBytes(0);
        }

        return dto;
    }

    @Override
    @Transactional(readOnly = true)
    public ExaminationResult getExaminationResultEntityById(Long id) {
        return examinationResultRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Examination result not found with ID: " + id));
    }



    @Override
    @Transactional(readOnly = true)
    public List<ExaminationResultResponseDTO> getAllExaminationResults() {
        return examinationResultRepository.findAll().stream()
                .map(result -> {
                    ExaminationResultResponseDTO dto = mapper.toDto(result);
                    if (result.getResultsReport() != null && !result.getResultsReport().trim().isEmpty()) {
                        dto.setHasReport(true);
                        dto.setReportSizeBytes(result.getResultsReport().length());
                        dto.setReportFormat("text/plain");
                    }
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ExaminationResultResponseDTO> getExaminationResultsByMedicalHistoryId(Long medicalHistoryId) {
        return examinationResultRepository.findByMedicalHistoryId(medicalHistoryId).stream()
                .map(result -> {
                    ExaminationResultResponseDTO dto = mapper.toDto(result);
                    if (result.getResultsReport() != null && !result.getResultsReport().trim().isEmpty()) {
                        dto.setHasReport(true);
                        dto.setReportSizeBytes(result.getResultsReport().length());
                        dto.setReportFormat("text/plain");
                    }
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ExaminationResultResponseDTO createExaminationResult(ExaminationResultRequestDTO requestDTO) {
        log.info("Creating examination result for medical history ID: {}", requestDTO.getMedicalHistoryId());

        // Verificar si ya existe
        if (hasResultForMedicalHistory(requestDTO.getMedicalHistoryId())) {
            throw new IllegalArgumentException("Ya existe un resultado para este historial médico");
        }

        // Buscar el historial médico
        MedicalHistory medicalHistory = medicalHistoryRepository.findById(requestDTO.getMedicalHistoryId())
                .orElseThrow(() -> new EntityNotFoundException("Medical history not found with ID: " + requestDTO.getMedicalHistoryId()));

        // Crear la entidad
        ExaminationResult result = mapper.toEntity(requestDTO);
        result.setMedicalHistory(medicalHistory);

        // Generar reporte automáticamente
        if (requestDTO.getResultsReportBase64() == null || requestDTO.getResultsReportBase64().trim().isEmpty()) {
            // Obtener información para generar el reporte
            String examType = getExamTypeFromMedicalHistory(medicalHistory);
            String laboratoryName = getLaboratoryNameFromMedicalHistory(medicalHistory);
            String patientName = medicalHistory.getPatient().getName();

            // Generar reporte automático variado
            String generatedReport = reportGenerator.generateReport(examType, laboratoryName, patientName);
            result.setResultsReport(generatedReport);

            log.info("Generated automatic {} report for patient: {}", examType, patientName);
        } else {
            // Procesar el reporte base64 proporcionado
            try {
                byte[] decodedBytes = Base64.getDecoder().decode(requestDTO.getResultsReportBase64());
                String reportText = new String(decodedBytes);
                result.setResultsReport(reportText);
                log.info("Successfully decoded and stored provided report, length: {} characters", reportText.length());
            } catch (IllegalArgumentException e) {
                log.error("Error decoding base64 report: {}", e.getMessage());
                throw new IllegalArgumentException("Invalid base64 format for results report");
            }
        }

        // Guardar
        ExaminationResult savedResult = examinationResultRepository.save(result);
        log.info("Successfully saved examination result with ID: {}", savedResult.getId());

        // Retornar DTO con información completa
        ExaminationResultResponseDTO dto = mapper.toDto(savedResult);
        if (savedResult.getResultsReport() != null && !savedResult.getResultsReport().trim().isEmpty()) {
            dto.setHasReport(true);
            dto.setReportSizeBytes(savedResult.getResultsReport().length());
            dto.setReportFormat("text/plain");
        }

        return dto;
    }

    @Override
    @Transactional
    public ExaminationResult createExaminationResult(ExaminationResult examinationResult) {
        // Puedes aplicar validaciones adicionales aquí si lo deseas
        return examinationResultRepository.save(examinationResult);
    }


    // Métodos helper para obtener información del contexto
    private String getExamTypeFromMedicalHistory(MedicalHistory medicalHistory) {
        // Buscar el último examen médico relacionado
        if (medicalHistory.getMedicalExaminations() != null && !medicalHistory.getMedicalExaminations().isEmpty()) {
            MedicalExamination lastExam = medicalHistory.getMedicalExaminations()
                    .stream()
                    .max((e1, e2) -> e1.getDateOfRealization().compareTo(e2.getDateOfRealization()))
                    .orElse(null);

            if (lastExam != null && lastExam.getTypeOfExam() != null) {
                return lastExam.getTypeOfExam().getName();
            }
        }
        return "Examen general";
    }

    private String getLaboratoryNameFromMedicalHistory(MedicalHistory medicalHistory) {
        // Buscar el laboratorio del último examen
        if (medicalHistory.getMedicalExaminations() != null && !medicalHistory.getMedicalExaminations().isEmpty()) {
            MedicalExamination lastExam = medicalHistory.getMedicalExaminations()
                    .stream()
                    .max((e1, e2) -> e1.getDateOfRealization().compareTo(e2.getDateOfRealization()))
                    .orElse(null);

            if (lastExam != null && lastExam.getLaboratory() != null) {
                return lastExam.getLaboratory().getName();
            }
        }
        return "Laboratorio Clínico";
    }

    @Override
    @Transactional
    public ExaminationResultResponseDTO updateExaminationResult(Long id, ExaminationResultUpdateDTO updateDTO) {
        ExaminationResult result = examinationResultRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Examination result not found with ID: " + id));

        mapper.updateEntityFromDto(updateDTO, result);
        ExaminationResult updatedResult = examinationResultRepository.save(result);

        ExaminationResultResponseDTO dto = mapper.toDto(updatedResult);
        if (updatedResult.getResultsReport() != null && !updatedResult.getResultsReport().trim().isEmpty()) {
            dto.setHasReport(true);
            dto.setReportSizeBytes(updatedResult.getResultsReport().length());
            dto.setReportFormat("text/plain");
        }

        return dto;
    }

    @Override
    @Transactional
    public void deleteExaminationResult(Long id) {
        if (!examinationResultRepository.existsById(id)) {
            throw new EntityNotFoundException("Examination result not found with ID: " + id);
        }
        examinationResultRepository.deleteById(id);
    }

    public boolean hasResultForMedicalHistory(Long medicalHistoryId) {
        return !examinationResultRepository.findByMedicalHistoryId(medicalHistoryId).isEmpty();
    }

    public String getResultReportAsBase64(Long resultId) {
        ExaminationResult result = examinationResultRepository.findById(resultId)
                .orElseThrow(() -> new EntityNotFoundException("Examination result not found with ID: " + resultId));

        if (result.getResultsReport() == null || result.getResultsReport().trim().isEmpty()) {
            return null;
        }

        // Convertir String a base64
        return Base64.getEncoder().encodeToString(result.getResultsReport().getBytes());
    }

    public String getResultReportAsText(Long resultId) {
        ExaminationResult result = examinationResultRepository.findById(resultId)
                .orElseThrow(() -> new EntityNotFoundException("Examination result not found with ID: " + resultId));

        return result.getResultsReport();
    }
}