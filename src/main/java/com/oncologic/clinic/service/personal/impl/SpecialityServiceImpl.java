package com.oncologic.clinic.service.personal.impl;

import com.oncologic.clinic.dto.personal.request.SpecialityRequestDTO;
import com.oncologic.clinic.dto.personal.response.SpecialityResponseDTO;
import com.oncologic.clinic.dto.personal.update.SpecialityUpdateDTO;
import com.oncologic.clinic.entity.personal.Speciality;
import com.oncologic.clinic.exception.runtime.personal.SpecialityNotFoundException;
import com.oncologic.clinic.mapper.personal.SpecialityMapper;
import com.oncologic.clinic.repository.personal.SpecialityRepository;
import com.oncologic.clinic.service.personal.SpecialityService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SpecialityServiceImpl implements SpecialityService {
    private final SpecialityRepository specialityRepository;
    private final SpecialityMapper specialityMapper;

    public SpecialityServiceImpl(SpecialityRepository specialityRepository, SpecialityMapper specialityMapper) {
        this.specialityRepository = specialityRepository;
        this.specialityMapper = specialityMapper;
    }

    @Override
    public SpecialityResponseDTO getSpecialityById(Long id) {
        Speciality speciality = specialityRepository.findById(id).orElseThrow(() -> new SpecialityNotFoundException(id));
        return specialityMapper.toDto(speciality);
    }

    @Override
    public List<SpecialityResponseDTO> getAllSpecialities() {
        return specialityRepository.findAll().stream().map(specialityMapper::toDto).toList();
    }

    @Override
    @Transactional
    public SpecialityResponseDTO createSpeciality(SpecialityRequestDTO specialityDTO) {
        Speciality speciality = specialityMapper.toEntity(specialityDTO);

        Speciality savedSpeciality = specialityRepository.save(speciality);
        return specialityMapper.toDto(savedSpeciality);
    }


    @Override
    public SpecialityResponseDTO updateSpeciality(Long id, SpecialityUpdateDTO specialityDTO) {
        Speciality existingSpeciality = specialityRepository.findById(id)
                .orElseThrow(() -> new SpecialityNotFoundException(id));

        specialityMapper.updateEntityFromDto(specialityDTO, existingSpeciality);

        Speciality updatedSpeciality = specialityRepository.save(existingSpeciality);

        return specialityMapper.toDto(updatedSpeciality);
    }

    @Override
    @Transactional
    public void deleteSpeciality(Long id) {
        if (!specialityRepository.existsById(id)) {
            throw new SpecialityNotFoundException(id);
        }
        specialityRepository.deleteById(id);
    }
}
