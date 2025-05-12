package com.oncologic.clinic.service.examination.impl;

import com.oncologic.clinic.dto.examination.TypeOfExamDTO;
import com.oncologic.clinic.dto.examination.response.TypeOfExamResponseDTO;
import com.oncologic.clinic.entity.examination.TypeOfExam;
import com.oncologic.clinic.exception.runtime.examination.TypeOfExamNotFoundException;
import com.oncologic.clinic.mapper.examination.TypeOfExamMapper;
import com.oncologic.clinic.repository.examination.TypeOfExamRepository;
import com.oncologic.clinic.service.examination.TypeOfExamService;
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
                .orElseThrow(() -> new TypeOfExamNotFoundException(id));
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
    public TypeOfExamResponseDTO createTypeOfExam(TypeOfExamDTO requestDTO) {
        TypeOfExam type = mapper.toEntity(requestDTO);
        TypeOfExam savedType = typeOfExamRepository.save(type);
        return mapper.toDto(savedType);
    }

    @Override
    @Transactional
    public TypeOfExamResponseDTO updateTypeOfExam(Long id, TypeOfExamDTO updateDTO) {
        TypeOfExam type = typeOfExamRepository.findById(id)
                .orElseThrow(() -> new TypeOfExamNotFoundException(id));

        mapper.updateEntityFromDto(updateDTO, type);
        TypeOfExam updatedType = typeOfExamRepository.save(type);
        return mapper.toDto(updatedType);
    }

    @Override
    @Transactional
    public void deleteTypeOfExam(Long id) {
        if (!typeOfExamRepository.existsById(id)) {
            throw new TypeOfExamNotFoundException(id);
        }
        typeOfExamRepository.deleteById(id);
    }
}