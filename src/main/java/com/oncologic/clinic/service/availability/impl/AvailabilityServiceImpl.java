package com.oncologic.clinic.service.availability.impl;

import com.oncologic.clinic.dto.availability.AvailabilityDTO;
import com.oncologic.clinic.dto.availability.response.AvailabilityResponseDTO;
import com.oncologic.clinic.entity.availability.Availability;
import com.oncologic.clinic.entity.personal.Personal;
import com.oncologic.clinic.exception.runtime.AvailabilityNotFoundException;
import com.oncologic.clinic.mapper.availability.AvailabilityMapper;
import com.oncologic.clinic.repository.availability.AvailabilityRepository;
import com.oncologic.clinic.repository.personal.PersonalRepository;
import com.oncologic.clinic.service.availability.AvailabilityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AvailabilityServiceImpl implements AvailabilityService {

    private final AvailabilityRepository availabilityRepository;
    private final PersonalRepository personalRepository;
    private final AvailabilityMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public AvailabilityResponseDTO getAvailabilityById(Long id) {
        Availability availability = availabilityRepository.findById(id).orElseThrow(() -> new AvailabilityNotFoundException("Availability not found with ID " + id));
        return mapper.toDto(availability);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AvailabilityResponseDTO> getAllAvailabilities() {
        return availabilityRepository.findAll().stream().map(mapper::toDto).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public AvailabilityResponseDTO createAvailability(AvailabilityDTO requestDTO) {
        Availability availability = mapper.toEntity(requestDTO);

        if (requestDTO.getPersonalIds() != null) updateAvailabilityPersonalsFromDTO(availability, requestDTO);

        Availability savedAvailability = availabilityRepository.save(availability);
        return mapper.toDto(savedAvailability);
    }

    @Override
    @Transactional
    public AvailabilityResponseDTO updateAvailability(Long id, AvailabilityDTO updateDTO) {
        Availability availability = availabilityRepository.findById(id).orElseThrow(() -> new AvailabilityNotFoundException("Availability not found with ID " + id));

        mapper.updateEntityFromDto(updateDTO, availability);

        if (updateDTO.getPersonalIds() != null && !updateDTO.getPersonalIds().isEmpty())
            updateAvailabilityPersonalsFromDTO(availability, updateDTO);

        Availability updatedAvailability = availabilityRepository.save(availability);
        return mapper.toDto(updatedAvailability);
    }

    @Override
    @Transactional
    public void deleteAvailability(Long id) {
        if (!availabilityRepository.existsById(id)) {
            throw new AvailabilityNotFoundException("Availability not found with ID " + id);
        }
        availabilityRepository.deleteById(id);
    }

    private void updateAvailabilityPersonalsFromDTO(Availability availability, AvailabilityDTO requestDTO) {
        Set<Personal> personals = new HashSet<>(personalRepository.findAllById(requestDTO.getPersonalIds()));
        System.out.println(personals);
        availability.getPersonals().clear();
        availability.getPersonals().addAll(personals);
    }
}