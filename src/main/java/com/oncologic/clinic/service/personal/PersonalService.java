package com.oncologic.clinic.service.personal;

import com.oncologic.clinic.dto.availability.response.AvailabilityResponseDTO;

import java.util.List;

public interface PersonalService {
    List<AvailabilityResponseDTO> getPersonalAvailabilities(Long personalId);
}
