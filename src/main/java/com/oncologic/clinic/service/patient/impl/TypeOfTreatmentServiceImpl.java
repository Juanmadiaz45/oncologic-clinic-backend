package com.oncologic.clinic.service.patient.impl;

import com.oncologic.clinic.dto.patient.request.TypeOfTreatmentRequestDTO;
import com.oncologic.clinic.dto.patient.response.TypeOfTreatmentResponseDTO;
import com.oncologic.clinic.entity.patient.Treatment;
import com.oncologic.clinic.entity.patient.TypeOfTreatment;
import com.oncologic.clinic.exception.runtime.patient.ResourceNotFoundException;
import com.oncologic.clinic.mapper.patient.TypeOfTreatmentMapper;
import com.oncologic.clinic.repository.patient.TreatmentRepository;
import com.oncologic.clinic.repository.patient.TypeOfTreatmentRepository;
import com.oncologic.clinic.service.patient.TypeOfTreatmentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TypeOfTreatmentServiceImpl implements TypeOfTreatmentService {
    private final TypeOfTreatmentRepository typeOfTreatmentRepository;
    private final TreatmentRepository treatmentRepository;
    private final TypeOfTreatmentMapper typeOfTreatmentMapper;

    public TypeOfTreatmentServiceImpl(TypeOfTreatmentRepository typeOfTreatmentRepository, TreatmentRepository treatmentRepository, TypeOfTreatmentMapper typeOfTreatmentMapper) {
        this.typeOfTreatmentRepository = typeOfTreatmentRepository;
        this.treatmentRepository = treatmentRepository;
        this.typeOfTreatmentMapper = typeOfTreatmentMapper;
    }

    @Override
    public TypeOfTreatmentResponseDTO getTypeOfTreatmentById(Long id) {
        TypeOfTreatment type = typeOfTreatmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Type of treatment not found with ID: " + id));
        return typeOfTreatmentMapper.toDto(type);
    }

    @Override
    public List<TypeOfTreatmentResponseDTO> getAllTypesOfTreatment() {
        return typeOfTreatmentRepository.findAll()
                .stream()
                .map(typeOfTreatmentMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public TypeOfTreatmentResponseDTO createTypeOfTreatment(TypeOfTreatmentRequestDTO dto) {
        Treatment treatment = treatmentRepository.findById(dto.getTreatmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Treatment not found with ID: " + dto.getTreatmentId()));

        TypeOfTreatment type = typeOfTreatmentMapper.toEntity(dto);
        type.setTreatment(treatment);

        TypeOfTreatment saved = typeOfTreatmentRepository.save(type);
        return typeOfTreatmentMapper.toDto(saved);
    }

    @Override
    @Transactional
    public TypeOfTreatmentResponseDTO updateTypeOfTreatment(Long id, TypeOfTreatmentRequestDTO dto) {
        TypeOfTreatment type = typeOfTreatmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Type of treatment not found with ID: " + id));

        if (dto.getTreatmentId() != null) {
            Treatment treatment = treatmentRepository.findById(dto.getTreatmentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Treatment not found with ID: " + dto.getTreatmentId()));
            type.setTreatment(treatment);
        }

        typeOfTreatmentMapper.updateEntityFromDto(dto, type);
        TypeOfTreatment updated = typeOfTreatmentRepository.save(type);
        return typeOfTreatmentMapper.toDto(updated);
    }

    @Override
    @Transactional
    public void deleteTypeOfTreatment(Long id) {
        TypeOfTreatment type = typeOfTreatmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Type of treatment not found with ID: " + id));
        typeOfTreatmentRepository.delete(type);
    }
}