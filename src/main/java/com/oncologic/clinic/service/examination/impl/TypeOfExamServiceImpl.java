package com.oncologic.clinic.service.examination.impl;

import com.oncologic.clinic.dto.examination.request.TypeOfExamRequestDTO;
import com.oncologic.clinic.dto.examination.response.TypeOfExamResponseDTO;
import com.oncologic.clinic.dto.examination.update.TypeOfExamUpdateDTO;
import com.oncologic.clinic.entity.examination.TypeOfExam;
import com.oncologic.clinic.mapper.examination.TypeOfExamMapper;
import com.oncologic.clinic.repository.examination.TypeOfExamRepository;
import com.oncologic.clinic.service.examination.TypeOfExamService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TypeOfExamServiceImpl implements TypeOfExamService {

    private final TypeOfExamRepository typeOfExamRepository;
    private final TypeOfExamMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public TypeOfExamResponseDTO getTypeOfExamById(Long id) {
        TypeOfExam type = typeOfExamRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Type of exam not found"));
        return mapper.toDto(type);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TypeOfExamResponseDTO> getAllTypesOfExam() {
        return typeOfExamRepository.findAll().stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public TypeOfExamResponseDTO createTypeOfExam(TypeOfExamRequestDTO requestDTO) {
        TypeOfExam type = mapper.toEntity(requestDTO);
        TypeOfExam savedType = typeOfExamRepository.save(type);
        return mapper.toDto(savedType);
    }

    @Override
    @Transactional
    public TypeOfExamResponseDTO updateTypeOfExam(Long id, TypeOfExamUpdateDTO updateDTO) {
        TypeOfExam type = typeOfExamRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Type of exam not found"));

        mapper.updateEntityFromDto(updateDTO, type);
        TypeOfExam updatedType = typeOfExamRepository.save(type);
        return mapper.toDto(updatedType);
    }

    @Override
    @Transactional
    public void deleteTypeOfExam(Long id) {
        if (!typeOfExamRepository.existsById(id)) {
            throw new EntityNotFoundException("Type of exam not found");
        }
        typeOfExamRepository.deleteById(id);
    }
}