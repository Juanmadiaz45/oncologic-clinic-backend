package com.oncologic.clinic.service.availability.impl;

import com.oncologic.clinic.dto.availability.AvailabilityDTO;
import com.oncologic.clinic.dto.availability.response.AvailabilityResponseDTO;
import com.oncologic.clinic.entity.availability.Availability;
import com.oncologic.clinic.entity.availability.Status;
import com.oncologic.clinic.entity.personal.Personal;
import com.oncologic.clinic.exception.runtime.availability.AvailabilityNotFoundException;
import com.oncologic.clinic.exception.runtime.availability.StatusNotFoundException;
import com.oncologic.clinic.exception.runtime.personal.PersonalNotFoundException;
import com.oncologic.clinic.mapper.availability.AvailabilityMapper;
import com.oncologic.clinic.repository.availability.AvailabilityRepository;
import com.oncologic.clinic.repository.availability.StatusRepository;
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
    private final StatusRepository statusRepository;
    private final AvailabilityMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public AvailabilityResponseDTO getAvailabilityById(Long id) {
        Availability availability = availabilityRepository.findById(id)
                .orElseThrow(() -> new AvailabilityNotFoundException("Availability not found with ID " + id));
        return mapper.toDto(availability);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AvailabilityResponseDTO> getAllAvailabilities() {
        return availabilityRepository.findAll().stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public AvailabilityResponseDTO createAvailability(AvailabilityDTO requestDTO) {
        Availability availability = mapper.toEntity(requestDTO);

        // Set status if provided
        if (requestDTO.getStatusId() != null) {
            Status status = statusRepository.findById(requestDTO.getStatusId())
                    .orElseThrow(() -> new StatusNotFoundException("Status not found with ID " + requestDTO.getStatusId()));
            availability.setStatus(status);
        }

        Availability savedAvailability = availabilityRepository.save(availability);
        // Set personals if provided
        if (requestDTO.getPersonalIds() != null && !requestDTO.getPersonalIds().isEmpty()) {
            updateAvailabilityPersonalsFromDTO(savedAvailability, requestDTO);
        }

        Availability reloadedAvailability = availabilityRepository.findById(savedAvailability.getId())
                .orElseThrow(() -> new AvailabilityNotFoundException(savedAvailability.getId()));
        return mapper.toDto(reloadedAvailability);
    }

    @Override
    @Transactional
    public AvailabilityResponseDTO updateAvailability(Long id, AvailabilityDTO updateDTO) {
        Availability availability = availabilityRepository.findById(id)
                .orElseThrow(() -> new AvailabilityNotFoundException("Availability not found with ID " + id));

        mapper.updateEntityFromDto(updateDTO, availability);

        // Update status if changed
        if (updateDTO.getStatusId() != null &&
                (availability.getStatus() == null || !availability.getStatus().getId().equals(updateDTO.getStatusId()))) {
            Status status = statusRepository.findById(updateDTO.getStatusId())
                    .orElseThrow(() -> new StatusNotFoundException("Status not found with ID " + updateDTO.getStatusId()));
            availability.setStatus(status);
        }

        // Update personals if changed
        if (updateDTO.getPersonalIds() != null) {
            updateAvailabilityPersonalsFromDTO(availability, updateDTO);
        }

        Availability reloadedAvailability = availabilityRepository.findById(availability.getId())
                .orElseThrow(() -> new AvailabilityNotFoundException(availability.getId()));
        return mapper.toDto(reloadedAvailability);
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
        // 1. ✅ GET current and new IDs
        Set<Long> currentPersonalIds = availability.getPersonals().stream()
                .map(Personal::getId)
                .collect(Collectors.toSet());

        Set<Long> newPersonalIds = requestDTO.getPersonalIds() != null ?
                new HashSet<>(requestDTO.getPersonalIds()) : new HashSet<>();

        // 2. ✅ CALCULATE differences
        Set<Long> toRemove = new HashSet<>(currentPersonalIds);
        toRemove.removeAll(newPersonalIds); // IDs that were there but are no longer there

        Set<Long> toAdd = new HashSet<>(newPersonalIds);
        toAdd.removeAll(currentPersonalIds); // New IDs that were not there before


        // 3. ✅ REMOVE obsolete associations
        if (!toRemove.isEmpty()) {
            List<Personal> personalsToRemove = personalRepository.findAllById(toRemove);
            for (Personal personal : personalsToRemove) {
                personal.removeAvailability(availability);
            }
            // Save all changes at once
            personalRepository.saveAll(personalsToRemove);
        }

        // 4. ✅ ADD new associations
        if (!toAdd.isEmpty()) {
            List<Personal> personalsToAdd = personalRepository.findAllById(toAdd);

            // Verify that all Personnel exist
            if (personalsToAdd.size() != toAdd.size()) {
                Set<Long> foundIds = personalsToAdd.stream()
                        .map(Personal::getId)
                        .collect(Collectors.toSet());
                Set<Long> missingIds = new HashSet<>(toAdd);
                missingIds.removeAll(foundIds);
                throw new PersonalNotFoundException("Personal not found with IDs: " + missingIds);
            }

            for (Personal personal : personalsToAdd) {
                personal.addAvailability(availability);
            }
            // Save all changes at once
            personalRepository.saveAll(personalsToAdd);
        }
    }
}