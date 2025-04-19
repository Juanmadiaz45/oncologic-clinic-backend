package com.oncologic.clinic.service.availability;

import com.oncologic.clinic.entity.availability.Availability;

import java.util.List;

public interface AvailabilityService {
    Availability getAvailabilityById(Long id);
    List<Availability> getAllAvailabilities();
    Availability createAvailability(Availability availability);
    Availability updateAvailability(Availability availability);
    void deleteAvailability(Long id);
}
