package com.oncologic.clinic.service.examination.impl;

import com.oncologic.clinic.dto.examination.request.ExaminationResultRequestDTO;
import com.oncologic.clinic.dto.examination.response.ExaminationResultResponseDTO;
import com.oncologic.clinic.dto.examination.update.ExaminationResultUpdateDTO;
import com.oncologic.clinic.entity.examination.ExaminationResult;
import com.oncologic.clinic.entity.patient.MedicalHistory;
import com.oncologic.clinic.exception.runtime.examination.ExaminationResultNotFoundException;
import com.oncologic.clinic.exception.runtime.patient.MedicalHistoryNotFoundException;
import com.oncologic.clinic.mapper.examination.ExaminationResultMapper;
import com.oncologic.clinic.repository.examination.ExaminationResultRepository;
import com.oncologic.clinic.repository.patient.MedicalHistoryRepository;
import com.oncologic.clinic.service.examination.ExaminationResultService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExaminationResultServiceImpl implements ExaminationResultService {

    private final ExaminationResultRepository examinationResultRepository;
    private final MedicalHistoryRepository medicalHistoryRepository;
    private final ExaminationResultMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public ExaminationResultResponseDTO getExaminationResultById(Long id) {
        ExaminationResult result = examinationResultRepository.findById(id)
                .orElseThrow(() -> new ExaminationResultNotFoundException(id));
        return mapper.toDto(result);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ExaminationResultResponseDTO> getAllExaminationResults() {
        return examinationResultRepository.findAll().stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ExaminationResultResponseDTO> getExaminationResultsByMedicalHistoryId(Long medicalHistoryId) {
        return examinationResultRepository.findByMedicalHistoryId(medicalHistoryId).stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ExaminationResultResponseDTO createExaminationResult(ExaminationResultRequestDTO requestDTO) {
        MedicalHistory medicalHistory = medicalHistoryRepository.findById(requestDTO.getMedicalHistoryId())
                .orElseThrow(() -> new MedicalHistoryNotFoundException(requestDTO.getMedicalHistoryId()));

        ExaminationResult result = mapper.toEntity(requestDTO);
        result.setMedicalHistory(medicalHistory);

        ExaminationResult savedResult = examinationResultRepository.save(result);
        return mapper.toDto(savedResult);
    }

    @Override
    @Transactional
    public ExaminationResultResponseDTO updateExaminationResult(Long id, ExaminationResultUpdateDTO updateDTO) {
        ExaminationResult result = examinationResultRepository.findById(id)
                .orElseThrow(() -> new ExaminationResultNotFoundException(id));

        mapper.updateEntityFromDto(updateDTO, result);
        ExaminationResult updatedResult = examinationResultRepository.save(result);
        return mapper.toDto(updatedResult);
    }

    @Override
    @Transactional
    public void deleteExaminationResult(Long id) {
        if (!examinationResultRepository.existsById(id)) {
            throw new ExaminationResultNotFoundException(id);
        }
        examinationResultRepository.deleteById(id);
    }
}