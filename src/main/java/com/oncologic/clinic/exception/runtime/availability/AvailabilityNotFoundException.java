package com.oncologic.clinic.exception.runtime.availability;

import com.oncologic.clinic.exception.runtime.DomainNotFoundException;

public class AvailabilityNotFoundException extends DomainNotFoundException {
    public AvailabilityNotFoundException(String message) {
        super(message);
    }

    public AvailabilityNotFoundException(Long id) {
        super(String.format("No availability found for id %d", id));
    }
}
