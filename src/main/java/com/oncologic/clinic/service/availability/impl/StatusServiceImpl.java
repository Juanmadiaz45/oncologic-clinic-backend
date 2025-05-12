package com.oncologic.clinic.service.availability.impl;

import com.oncologic.clinic.dto.availability.StatusDTO;
import com.oncologic.clinic.dto.availability.response.StatusResponseDTO;
import com.oncologic.clinic.entity.availability.Availability;
import com.oncologic.clinic.entity.availability.Status;
import com.oncologic.clinic.exception.runtime.availability.AvailabilityNotFoundException;
import com.oncologic.clinic.exception.runtime.availability.StatusNotFoundException;
import com.oncologic.clinic.mapper.availability.StatusMapper;
import com.oncologic.clinic.repository.availability.AvailabilityRepository;
import com.oncologic.clinic.repository.availability.StatusRepository;
import com.oncologic.clinic.service.availability.StatusService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StatusServiceImpl implements StatusService {

    private final StatusRepository statusRepository;
    private final AvailabilityRepository availabilityRepository;
    private final StatusMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public StatusResponseDTO getStatusById(Long id) {
        Status status = statusRepository.findById(id)
                .orElseThrow(() -> new StatusNotFoundException("Status not found with ID " + id));
        return mapper.toDto(status);
    }

    @Override
    @Transactional(readOnly = true)
    public List<StatusResponseDTO> getAllStatuses() {
        return statusRepository.findAll().stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public StatusResponseDTO createStatus(StatusDTO requestDTO) {
        Status status = mapper.toEntity(requestDTO);

        if (requestDTO.getAvailabilityId() != null) {
            Availability availability = availabilityRepository.findById(requestDTO.getAvailabilityId())
                    .orElseThrow(() -> new AvailabilityNotFoundException("Availability not found"));
            status.setAvailability(availability);
        }

        Status savedStatus = statusRepository.save(status);
        return mapper.toDto(savedStatus);
    }

    @Override
    @Transactional
    public StatusResponseDTO updateStatus(Long id, StatusDTO updateDTO) {
        Status status = statusRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Status not found"));

        mapper.updateEntityFromDto(updateDTO, status);

        if (updateDTO.getAvailabilityId() != null) {
            Availability availability = availabilityRepository.findById(updateDTO.getAvailabilityId())
                    .orElseThrow(() -> new AvailabilityNotFoundException("Availability not found"));
            status.setAvailability(availability);
        }

        Status updatedStatus = statusRepository.save(status);
        return mapper.toDto(updatedStatus);
    }

    @Override
    @Transactional
    public void deleteStatus(Long id) {
        if (!statusRepository.existsById(id)) {
            throw new StatusNotFoundException("Status not found with ID " + id);
        }
        statusRepository.deleteById(id);
    }
}