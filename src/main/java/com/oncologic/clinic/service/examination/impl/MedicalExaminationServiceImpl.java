package com.oncologic.clinic.service.examination.impl;

import com.oncologic.clinic.dto.examination.request.MedicalExaminationRequestDTO;
import com.oncologic.clinic.dto.examination.response.MedicalExaminationResponseDTO;
import com.oncologic.clinic.dto.examination.update.MedicalExaminationUpdateDTO;
import com.oncologic.clinic.entity.examination.Laboratory;
import com.oncologic.clinic.entity.examination.MedicalExamination;
import com.oncologic.clinic.entity.examination.TypeOfExam;
import com.oncologic.clinic.entity.patient.MedicalHistory;
import com.oncologic.clinic.mapper.examination.MedicalExaminationMapper;
import com.oncologic.clinic.repository.examination.LaboratoryRepository;
import com.oncologic.clinic.repository.examination.MedicalExaminationRepository;
import com.oncologic.clinic.repository.examination.TypeOfExamRepository;
import com.oncologic.clinic.repository.patient.MedicalHistoryRepository;
import com.oncologic.clinic.service.examination.MedicalExaminationService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MedicalExaminationServiceImpl implements MedicalExaminationService {

    private final MedicalExaminationRepository medicalExaminationRepository;
    private final LaboratoryRepository laboratoryRepository;
    private final TypeOfExamRepository typeOfExamRepository;
    private final MedicalHistoryRepository medicalHistoryRepository;
    private final MedicalExaminationMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public MedicalExaminationResponseDTO getMedicalExaminationById(String id) {
        MedicalExamination exam = medicalExaminationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Medical examination not found"));
        return mapper.toDto(exam);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MedicalExaminationResponseDTO> getAllMedicalExaminations() {
        return medicalExaminationRepository.findAll().stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public MedicalExaminationResponseDTO createMedicalExamination(MedicalExaminationRequestDTO requestDTO) {
        Laboratory lab = laboratoryRepository.findById(requestDTO.getLaboratoryId())
                .orElseThrow(() -> new EntityNotFoundException("Laboratory not found"));

        TypeOfExam type = typeOfExamRepository.findById(requestDTO.getTypeOfExamId())
                .orElseThrow(() -> new EntityNotFoundException("Type of exam not found"));

        MedicalHistory history = medicalHistoryRepository.findById(requestDTO.getMedicalHistoryId())
                .orElseThrow(() -> new EntityNotFoundException("Medical history not found"));

        MedicalExamination exam = mapper.toEntity(requestDTO);
        exam.setLaboratory(lab);
        exam.setTypeOfExam(type);
        exam.setMedicalHistory(history);

        MedicalExamination savedExam = medicalExaminationRepository.save(exam);
        return mapper.toDto(savedExam);
    }

    @Override
    @Transactional
    public MedicalExaminationResponseDTO updateMedicalExamination(String id, MedicalExaminationUpdateDTO updateDTO) {
        MedicalExamination exam = medicalExaminationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Medical examination not found"));

        Laboratory lab = laboratoryRepository.findById(updateDTO.getLaboratoryId())
                .orElseThrow(() -> new EntityNotFoundException("Laboratory not found"));

        TypeOfExam type = typeOfExamRepository.findById(updateDTO.getTypeOfExamId())
                .orElseThrow(() -> new EntityNotFoundException("Type of exam not found"));

        mapper.updateEntityFromDto(updateDTO, exam);
        exam.setLaboratory(lab);
        exam.setTypeOfExam(type);

        MedicalExamination updatedExam = medicalExaminationRepository.save(exam);
        return mapper.toDto(updatedExam);
    }

    @Override
    @Transactional
    public void deleteMedicalExamination(String id) {
        if (!medicalExaminationRepository.existsById(id)) {
            throw new EntityNotFoundException("Medical examination not found");
        }
        medicalExaminationRepository.deleteById(id);
    }
}