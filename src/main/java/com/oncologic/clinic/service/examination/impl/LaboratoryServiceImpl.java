package com.oncologic.clinic.service.examination.impl;

import com.oncologic.clinic.dto.examination.request.LaboratoryRequestDTO;
import com.oncologic.clinic.dto.examination.response.LaboratoryResponseDTO;
import com.oncologic.clinic.dto.examination.update.LaboratoryUpdateDTO;
import com.oncologic.clinic.entity.examination.Laboratory;
import com.oncologic.clinic.mapper.examination.LaboratoryMapper;
import com.oncologic.clinic.repository.examination.LaboratoryRepository;
import com.oncologic.clinic.service.examination.LaboratoryService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LaboratoryServiceImpl implements LaboratoryService {

    private final LaboratoryRepository laboratoryRepository;
    private final LaboratoryMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public LaboratoryResponseDTO getLaboratoryById(Long id) {
        Laboratory lab = laboratoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Laboratory not found"));
        return mapper.toDto(lab);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LaboratoryResponseDTO> getAllLaboratories() {
        return laboratoryRepository.findAll().stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public LaboratoryResponseDTO createLaboratory(LaboratoryRequestDTO requestDTO) {
        Laboratory lab = mapper.toEntity(requestDTO);
        Laboratory savedLab = laboratoryRepository.save(lab);
        return mapper.toDto(savedLab);
    }

    @Override
    @Transactional
    public LaboratoryResponseDTO updateLaboratory(Long id, LaboratoryUpdateDTO updateDTO) {
        Laboratory lab = laboratoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Laboratory not found"));

        mapper.updateEntityFromDto(updateDTO, lab);
        Laboratory updatedLab = laboratoryRepository.save(lab);
        return mapper.toDto(updatedLab);
    }

    @Override
    @Transactional
    public void deleteLaboratory(Long id) {
        if (!laboratoryRepository.existsById(id)) {
            throw new EntityNotFoundException("Laboratory not found");
        }
        laboratoryRepository.deleteById(id);
    }
}