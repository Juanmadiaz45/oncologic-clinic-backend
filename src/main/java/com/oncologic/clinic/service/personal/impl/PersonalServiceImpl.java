package com.oncologic.clinic.service.personal.impl;

import com.oncologic.clinic.dto.availability.response.AvailabilityResponseDTO;
import com.oncologic.clinic.entity.personal.Personal;
import com.oncologic.clinic.exception.runtime.personal.PersonalNotFoundException;
import com.oncologic.clinic.mapper.availability.AvailabilityMapper;
import com.oncologic.clinic.repository.personal.PersonalRepository;
import com.oncologic.clinic.service.personal.PersonalService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PersonalServiceImpl implements PersonalService {

    private final PersonalRepository personalRepository;
    private final AvailabilityMapper availabilityMapper;

    @Override
    public List<AvailabilityResponseDTO> getPersonalAvailabilities(Long personalId) {
        Personal personal = personalRepository.findById(personalId)
                .orElseThrow(() -> new PersonalNotFoundException("Personal not found with ID: " + personalId));

        return personal.getAvailabilities()
                .stream()
                .map(availabilityMapper::toDto)
                .toList();
    }
}