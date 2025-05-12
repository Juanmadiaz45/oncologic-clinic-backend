package com.oncologic.clinic.exception.runtime.patient;

import com.oncologic.clinic.exception.runtime.DomainNotFoundException;

public class AppointmentResultNotFoundException extends DomainNotFoundException {
    public AppointmentResultNotFoundException(Long id) {
        super("AppointmentResult not found with ID: " + id);
    }
}