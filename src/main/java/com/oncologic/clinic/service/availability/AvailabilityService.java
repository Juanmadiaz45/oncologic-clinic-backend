package com.oncologic.clinic.service.availability;

import com.oncologic.clinic.dto.availability.AvailabilityDTO;
import com.oncologic.clinic.dto.availability.response.AvailabilityResponseDTO;

import java.util.List;

public interface AvailabilityService {
    AvailabilityResponseDTO getAvailabilityById(Long id);
    List<AvailabilityResponseDTO> getAllAvailabilities();
    AvailabilityResponseDTO createAvailability(AvailabilityDTO requestDTO);
    AvailabilityResponseDTO updateAvailability(Long id, AvailabilityDTO updateDTO);
    void deleteAvailability(Long id);
}
