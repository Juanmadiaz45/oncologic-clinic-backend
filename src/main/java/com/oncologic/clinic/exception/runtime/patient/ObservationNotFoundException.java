package com.oncologic.clinic.exception.runtime.patient;

import com.oncologic.clinic.exception.runtime.DomainNotFoundException;

public class ObservationNotFoundException extends DomainNotFoundException {
    public ObservationNotFoundException(Long id) {
        super("Observation not found with ID: " + id);
    }
}
