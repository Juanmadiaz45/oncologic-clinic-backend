package com.oncologic.clinic.exception.runtime.patient;

public class ObservationNotFoundException extends RuntimeException {
    public ObservationNotFoundException(Long id) {
        super("Observation not found with ID: " + id);
    }
}
