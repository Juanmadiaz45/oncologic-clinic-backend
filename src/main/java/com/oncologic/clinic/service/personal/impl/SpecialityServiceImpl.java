package com.oncologic.clinic.service.personal.impl;

import com.oncologic.clinic.dto.personal.request.SpecialityRequestDTO;
import com.oncologic.clinic.dto.personal.response.SpecialityResponseDTO;
import com.oncologic.clinic.dto.personal.update.SpecialityUpdateDTO;
import com.oncologic.clinic.entity.personal.Speciality;
import com.oncologic.clinic.mapper.PersonalMapper;
import com.oncologic.clinic.repository.personal.SpecialityRepository;
import com.oncologic.clinic.service.personal.SpecialityService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SpecialityServiceImpl implements SpecialityService {
    private final SpecialityRepository specialityRepository;
    private final PersonalMapper personalMapper;

    public SpecialityServiceImpl(SpecialityRepository specialityRepository, PersonalMapper personalMapper) {
        this.specialityRepository = specialityRepository;
        this.personalMapper = personalMapper;
    }

    @Override
    public SpecialityResponseDTO getSpecialityById(Long id) {
        Speciality speciality = specialityRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Especialidad con el ID " + id + " no encontrada"));
        return personalMapper.toDto(speciality);
    }

    @Override
    public List<SpecialityResponseDTO> getAllSpecialities() {
        return specialityRepository.findAll().stream().map(personalMapper::toDto).toList();
    }

    @Override
    @Transactional
    public SpecialityResponseDTO createSpeciality(SpecialityRequestDTO specialityDTO) {
        Speciality speciality = personalMapper.toEntity(specialityDTO);

        Speciality savedSpeciality = specialityRepository.save(speciality);
        return personalMapper.toDto(savedSpeciality);
    }


    @Override
    public SpecialityResponseDTO updateSpeciality(Long id, SpecialityUpdateDTO specialityDTO) {
        Speciality existingSpeciality = specialityRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Especialidad con el ID " + id + " no encontrada"));

        personalMapper.updateEntityFromDto(specialityDTO, existingSpeciality);

        Speciality updatedSpeciality = specialityRepository.save(existingSpeciality);

        return personalMapper.toDto(updatedSpeciality);
    }

    @Override
    @Transactional
    public void deleteSpeciality(Long id) {
        if (!specialityRepository.existsById(id)) {
            throw new EntityNotFoundException("Especialidad con el ID " + id + " no encontrada");
        }
        specialityRepository.deleteById(id);
    }
}
