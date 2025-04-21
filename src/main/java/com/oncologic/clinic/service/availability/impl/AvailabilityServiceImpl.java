package com.oncologic.clinic.service.availability.impl;

import com.oncologic.clinic.entity.availability.Availability;
import com.oncologic.clinic.service.availability.AvailabilityService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AvailabilityServiceImpl implements AvailabilityService {
    @Override
    public Availability getAvailabilityById(Long id) {
        return null;
    }

    @Override
    public List<Availability> getAllAvailabilities() {
        return List.of();
    }

    @Override
    public Availability createAvailability(Availability availability) {
        return null;
    }

    @Override
    public Availability updateAvailability(Availability availability) {
        return null;
    }

    @Override
    public void deleteAvailability(Long id) {

    }
}